using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using System.Data.SQLite;

using ActivityInfo.Api;
using System.Windows.Forms;

namespace ActivityInfo.Sync
{
    class LocalDb
    {
        private ActivityInfoService service;
        private SQLiteConnection connection;

        private List<SyncRegion>.Enumerator regionEnum;
        private bool currentRegionIsComplete = true;

        public LocalDb(ActivityInfoService service)
        {
            this.service = service;

            var builder = new SQLiteConnectionStringBuilder();
            builder.DataSource = Application.UserAppDataPath + "\\ActivityInfo.db";

            connection = new SQLiteConnection(builder.ToString());
            connection.Open();

            executeSQL("create table if not exists sync_regions (id TEXT, localVersion INTEGER)");
            executeSQL("create table if not exists sync_history (lastUpdate INTEGER)");

            regionEnum = service.GetSyncRegions().List.GetEnumerator();
        }

        public SQLiteConnection Connection
        {
            get
            {
                return connection;
            }
        }

        public bool NextSyncStep(SyncLogger logger)
        {
            if (currentRegionIsComplete)
            {
                if (!regionEnum.MoveNext())
                {
                    return false;
                }
            }

            SyncRegion region = regionEnum.Current;
            string localVersion = queryLocalVersion(region.Id);

            logger.log(1, "Requesting updates for region '" + region.Id + "', localVersion = " + localVersion);

            SyncRegionUpdates updates;

           
            updates = service.GetSyncRegionUpdates(region.Id, localVersion);

            if (updates.Sql != null)
            {
                foreach (SyncRegionUpdate update in updates.Sql)
                {
                    executeUpdate(logger, update);
                }
            }

            updateLocalVersion(region.Id, updates.Version);
            currentRegionIsComplete = updates.Complete;


            logger.log(1, "    now at version " + updates.Version + " [complete=" + updates.Complete + "]");

            return true;
        }
        

        private int executeUpdate(SyncLogger logger, SyncRegionUpdate update)
        {
            logger.log(2, update.Statement);

            SQLiteCommand cmd = new SQLiteCommand(connection);
            cmd.CommandText = update.Statement;

            if (update.Executions == null)
            {
                return cmd.ExecuteNonQuery();
            }
            else
            {
                int rowsAffected = 0;
                foreach (ParameterSet set in update.Executions)
                {
                    logger.log(3, string.Join(",", set));

                    cmd.Parameters.Clear();
                    foreach (string param in set)
                    {
                        SQLiteParameter p = new SQLiteParameter();
                        p.Value = param;

                        cmd.Parameters.Add(p);
                    }
                    rowsAffected += cmd.ExecuteNonQuery();
                }
                return rowsAffected;
            }
        }

        private int executeSQL(String sql)
        {
            SQLiteCommand cmd = new SQLiteCommand(sql, connection);
            return cmd.ExecuteNonQuery();
        }

        private String queryLocalVersion(String regionId)
        {
            SQLiteCommand cmd = new SQLiteCommand(connection);
            cmd.CommandText = "select localVersion from sync_regions where id = :id";
            cmd.Parameters.Add(new SQLiteParameter("id", regionId));

            object result = cmd.ExecuteScalar();
            return result == null ? null : result.ToString();
        }

        private void updateLocalVersion(string regionId, string localVersion)
        {
            SQLiteCommand cmd = new SQLiteCommand(connection);
            cmd.CommandText = "update sync_regions set localVersion = :localVersion where id = :id";
            cmd.Parameters.Add(new SQLiteParameter("localVersion", localVersion));
            cmd.Parameters.Add(new SQLiteParameter("id", regionId));

            if(cmd.ExecuteNonQuery() == 0) {
                cmd = new SQLiteCommand(connection);
                cmd.CommandText = "insert into sync_regions (id, localVersion) values (:id, :localVersion)";
                cmd.Parameters.Add(new SQLiteParameter("id", regionId));
                cmd.Parameters.Add(new SQLiteParameter("localVersion", localVersion));
                cmd.ExecuteNonQuery();
            }
        }
    }  
}

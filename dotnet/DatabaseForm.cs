using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.SQLite;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

using ActivityInfo.Api;
using ActivityInfo.Sync;
using System.Net;

namespace ActivityInfo
{
    public partial class DatabaseForm : Form, SyncLogger
    {
        private ActivityInfoService service;
        private LocalDb localDb;

        public DatabaseForm()
        {
            InitializeComponent();
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }

        private void DatabaseForm_Load(object sender, EventArgs e)
        {
            LoginForm loginForm = new LoginForm();
            loginForm.ShowDialog(this);

            if (loginForm.Service == null)
            {
                return;
            }

            this.service = loginForm.Service;
            this.localDb = new LocalDb(service);

        }
        private void synchronizeToolStripMenuItem_Click(object sender, EventArgs eventArgs)
        {
            try
            {
                this.localDb.NextSyncStep(this);

                refreshTables();
            }
            catch (WebException e)
            {
                MessageBox.Show("Connection problem, SC = " + e.Message);
            }
               
        }

        private void refreshTables()
        {
            SQLiteCommand cmd = new SQLiteCommand(localDb.Connection);
            cmd.CommandText = "select name from sqlite_master";
            SQLiteDataReader reader = cmd.ExecuteReader();

            tableListView.Clear();

            while (reader.Read())
            {
                tableListView.Items.Add(reader.GetString(0));
            }
        }

        private void refreshToolStripMenuItem_Click(object sender, EventArgs e)
        {
            refreshTables();
        }

        private void tableListView_SelectedIndexChanged(object sender, EventArgs e)
        {
            string table = tableListView.FocusedItem.Text;
            SQLiteDataAdapter adapter = new SQLiteDataAdapter("select * from " + table, localDb.Connection);
            DataSet ds = new DataSet();
            adapter.Fill(ds);

            dataGridView.DataSource = ds.Tables[0];
        }

        private void dataGridView_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        #region SyncLogger Members

        public void log(int level, string text)
        {
            loggingTextBox.Text += text + "\r\n";
        }

        #endregion

    }
}

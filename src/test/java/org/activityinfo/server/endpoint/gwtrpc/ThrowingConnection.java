package org.activityinfo.server.endpoint.gwtrpc;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class ThrowingConnection implements Connection {

    private boolean open = true;

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearWarnings() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() throws SQLException {
        open = false;
    }

    @Override
    public void commit() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements)
        throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Blob createBlob() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Clob createClob() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public NClob createNClob() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Statement createStatement() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency)
        throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Statement createStatement(int resultSetType,
        int resultSetConcurrency, int resultSetHoldability)
        throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes)
        throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCatalog() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getHoldability() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return !open;
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType,
        int resultSetConcurrency) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType,
        int resultSetConcurrency, int resultSetHoldability)
        throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
        throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
        throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames)
        throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
        int resultSetConcurrency) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
        int resultSetConcurrency, int resultSetHoldability)
        throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void rollback() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setClientInfo(Properties properties)
        throws SQLClientInfoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setClientInfo(String name, String value)
        throws SQLClientInfoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public void setSchema(String schema) throws SQLException {
    }

    public String getSchema() throws SQLException {
        return null;
    }

    public void abort(Executor executor) throws SQLException {
    }

    public void setNetworkTimeout(Executor executor, int milliseconds)
        throws SQLException {
    }

    public int getNetworkTimeout() throws SQLException {
        return 0;
    }
}

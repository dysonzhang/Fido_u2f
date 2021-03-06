package fr.neowave.dao.mysql;

import fr.neowave.beans.Registration;
import fr.neowave.dao.interfaces.RegistrationDao;

import java.io.*;
import java.security.cert.X509Certificate;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RegistrationDaoImpl implements RegistrationDao   {


    private Connection connection;

    public RegistrationDaoImpl( Connection connection ) {
        this.connection = connection;
    }


    @Override
    public void create(Registration registration) throws SQLException, IOException {

        PreparedStatement preparedStatement= null;

        connection.setAutoCommit(false);
        try {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(registration.getCertificate());
            Blob data = connection.createBlob();
            data.setBytes(1, bos.toByteArray());
            bos.close();
            preparedStatement = connection.prepareStatement("INSERT INTO registrations (username, publicKey, certificate, counter, keyHandle, date, hostname, suspended) " +
                    "VALUES (?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, registration.getUsername());
            preparedStatement.setString(2, registration.getPublicKey());
            preparedStatement.setBlob(3, data);
            preparedStatement.setLong(4, registration.getCounter());
            preparedStatement.setString(5, registration.getKeyHandle());
            preparedStatement.setString(6, registration.getTimestamp());
            preparedStatement.setString(7, registration.getHostname());
            preparedStatement.setString(8, String.valueOf(registration.getSuspended()));

            if(preparedStatement.executeUpdate() == 1){
                connection.commit();
            }
            else {
                throw new SQLException("Insertion error");
            }


        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch(SQLException ex) {
                    throw new SQLException(ex);
                }
                throw new SQLException(e);
            }
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }

    @Override
    public void updateCounter(Registration registration) throws SQLException {

        PreparedStatement preparedStatement= null;

        try {
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement("UPDATE registrations SET counter = ? WHERE username = ? AND keyHandle = ?");
            preparedStatement.setLong(1, registration.getCounter());
            preparedStatement.setString(2, registration.getUsername());
            preparedStatement.setString(3, registration.getKeyHandle());

            if(preparedStatement.executeUpdate() == 1){
                connection.commit();
            }
            else {
                throw new SQLException("Can't update counter");
            }

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch(SQLException ex) {
                    throw new SQLException(ex);
                }
                throw new SQLException(e);
            }
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }

    @Override
    public void updateSuspended(Registration registration) throws SQLException {

        PreparedStatement preparedStatement= null;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement("UPDATE registrations SET suspended = ? WHERE username = ? AND keyHandle = ?");
            preparedStatement.setString(1, String.valueOf(registration.getSuspended()));
            preparedStatement.setString(2, registration.getUsername());
            preparedStatement.setString(3, registration.getKeyHandle());

            if(preparedStatement.executeUpdate() == 1){
                connection.commit();
            }
            else {
                throw new SQLException("Insertion error");
            }

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch(SQLException ex) {
                    throw new SQLException(ex);
                }
                throw new SQLException(e);
            }
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }



    @Override
    public void delete(Registration registration) throws SQLException  {
        PreparedStatement preparedStatement= null;

        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement("DELETE FROM registrations WHERE username = ? AND keyHandle = ?");
            preparedStatement.setString(1, registration.getUsername());
            preparedStatement.setString(2, registration.getKeyHandle());

            if(preparedStatement.executeUpdate() == 1){
                connection.commit();
            }
            else {
                throw new SQLException("Insertion error");
            }

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch(SQLException ex) {
                    throw new SQLException(ex);
                }
                throw new SQLException(e);
            }
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }

    @Override
    public List<Registration> list() throws SQLException, ClassNotFoundException, IOException, ParseException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Registration> list = new ArrayList<>();
        Registration registration;
        ByteArrayInputStream bis;
        ObjectInput in;

        try {

            preparedStatement = connection.prepareStatement("SELECT * FROM registrations ORDER BY username ASC");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                bis = new ByteArrayInputStream(resultSet.getBlob(3).getBytes(1, (int) resultSet.getBlob(3).length()));
                in = new ObjectInputStream(bis);
                X509Certificate cert = (X509Certificate) in.readObject();
                bis.close();
                registration = new Registration();
                registration.setUsername(resultSet.getString(1));
                registration.setPublicKey(resultSet.getString(2));
                registration.setCertificate(cert);
                registration.setCounter(resultSet.getLong(4));
                registration.setKeyHandle(resultSet.getString(5));
                registration.setTimestamp(resultSet.getString(6));
                registration.setHostname(resultSet.getString(7));
                registration.setSuspended(Boolean.valueOf(resultSet.getString(8)));
                list.add(registration);
            }


        } catch (SQLException e) {

            throw new SQLException(e);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException(e.getMessage());
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (connection != null) {
                connection.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return list;
    }

    @Override
    public List<Registration> list(String username) throws SQLException, IOException, ClassNotFoundException, ParseException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Registration> list = new ArrayList<>();
        Registration registration;
        ByteArrayInputStream bis;
        ObjectInput in;

        try {

            preparedStatement = connection.prepareStatement("SELECT * FROM registrations WHERE username = ? ORDER BY keyHandle ASC");
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){

                bis = new ByteArrayInputStream(resultSet.getBlob(3).getBytes(1, (int) resultSet.getBlob(3).length()));
                in = new ObjectInputStream(bis);
                X509Certificate cert = (X509Certificate) in.readObject();
                bis.close();
                registration = new Registration();
                registration.setUsername(resultSet.getString(1));
                registration.setPublicKey(resultSet.getString(2));
                registration.setCertificate(cert);
                registration.setCounter(resultSet.getLong(4));
                registration.setKeyHandle(resultSet.getString(5));
                registration.setTimestamp(resultSet.getString(6));
                registration.setHostname(resultSet.getString(7));
                registration.setSuspended(Boolean.valueOf(resultSet.getString(8)));
                list.add(registration);
            }


        } catch (SQLException e) {

            throw new SQLException(e);

        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException(e.getMessage());
        } catch (IOException e) {
            throw new IOException(e);
        }finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (connection != null) {
                connection.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }

        return list;
    }

    @Override
    public Registration getRegistration(String username, String keyHandle) throws SQLException, ClassNotFoundException, IOException, ParseException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Registration registration = null;
        ByteArrayInputStream bis;
        ObjectInput in;
        try {

            preparedStatement = connection.prepareStatement("SELECT * FROM registrations WHERE username = ? AND keyHandle = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, keyHandle);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                bis = new ByteArrayInputStream(resultSet.getBlob(3).getBytes(1, (int) resultSet.getBlob(3).length()));
                in = new ObjectInputStream(bis);
                X509Certificate cert = (X509Certificate) in.readObject();
                bis.close();
                registration = new Registration();
                registration.setUsername(resultSet.getString(1));
                registration.setPublicKey(resultSet.getString(2));
                registration.setCertificate(cert);
                registration.setCounter(resultSet.getLong(4));
                registration.setKeyHandle(resultSet.getString(5));
                registration.setTimestamp(resultSet.getString(6));
                registration.setHostname(resultSet.getString(7));
                registration.setSuspended(Boolean.valueOf(resultSet.getString(8)));
            }




        } catch (SQLException e) {
            throw new SQLException(e);

        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException(e.getMessage());
        } catch (IOException e) {
            throw new IOException(e);
        }finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (connection != null) {
                connection.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }

        return registration;
    }
}

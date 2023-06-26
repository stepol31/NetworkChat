package client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientConnectionTest {

    @Mock
    private Socket socket;

    @Mock
    private BufferedReader reader;

    @InjectMocks
    @Spy
    private ClientConnection connection;

    @Test
    void clientStart() throws IOException {
        doReturn("test user").when(connection).setName();
        doNothing().when(connection).setReaderAndWriter();

        connection.clientStart();

        verify(connection).setName();
        verify(connection).setReaderAndWriter();
    }

    @Test
    void setName() throws IOException {
        doReturn("UserName").when(reader).readLine();

        String actual = connection.setName();

        assertEquals("UserName", actual);
    }

    @Test
    void setName_when_throws_exception() throws IOException {
        doThrow(new IOException("")).when(reader).readLine();

        assertThrows(IOException.class, () -> connection.setName());
    }

    @Test
    void closeService_when_socket_is_opened() throws IOException {
        doReturn(false).when(socket).isClosed();
        InputStream inputStream = mock(InputStream.class);
        OutputStream outputStream = mock(OutputStream.class);
        doReturn(inputStream).when(socket).getInputStream();
        doReturn(outputStream).when(socket).getOutputStream();
        doNothing().when(inputStream).close();
        doNothing().when(outputStream).close();

        connection.closeService();

        verify(socket.getInputStream()).close();
        verify(socket.getOutputStream()).close();
    }

    @Test
    void closeService_when_socket_is_closed() throws IOException {
        doReturn(true).when(socket).isClosed();
        InputStream inputStream = mock(InputStream.class);
        OutputStream outputStream = mock(OutputStream.class);
        doReturn(inputStream).when(socket).getInputStream();
        doReturn(outputStream).when(socket).getOutputStream();

        connection.closeService();

        verify(socket.getInputStream(), never()).close();
        verify(socket.getOutputStream(), never()).close();
    }
}
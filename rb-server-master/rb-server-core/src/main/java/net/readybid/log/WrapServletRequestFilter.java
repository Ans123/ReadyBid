package net.readybid.log;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Created by DejanK on 12/14/2016.
 *
 */
public class WrapServletRequestFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(new ReReadableHttpServletRequest((HttpServletRequest) servletRequest), servletResponse);
    }

    private class ReReadableHttpServletRequest extends HttpServletRequestWrapper{

        private final String requestData;

        public ReReadableHttpServletRequest(HttpServletRequest servletRequest) throws IOException {
            super(servletRequest);

            final InputStream inputStream = servletRequest.getInputStream();
            final BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            requestData = buffer.lines().collect(Collectors.joining("\n"));
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return new BufferedServletInputStream(requestData);
        }
    }

    public class BufferedServletInputStream extends ServletInputStream {

        private byte[] myBytes;
        private int lastIndexRetrieved = -1;
        private ReadListener readListener = null;

        public BufferedServletInputStream(String str) {
            try {
                myBytes = str.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException("JVM did not support UTF-8", e);
            }
        }

        @Override
        public boolean isFinished() {
            return (lastIndexRetrieved == myBytes.length-1);
        }

        @Override
        public boolean isReady() {
            // This implementation will never block
            // We also never need to call the readListener from this method, as this method will never return false
            return isFinished();
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            this.readListener = readListener;
            if (!isFinished()) {
                try {
                    readListener.onDataAvailable();
                } catch (IOException e) {
                    readListener.onError(e);
                }
            } else {
                try {
                    readListener.onAllDataRead();
                } catch (IOException e) {
                    readListener.onError(e);
                }
            }
        }

        @Override
        public int read() throws IOException {
            int i;
            if (!isFinished()) {
                i = myBytes[lastIndexRetrieved+1];
                lastIndexRetrieved++;
                if (isFinished() && (readListener != null)) {
                    try {
                        readListener.onAllDataRead();
                    } catch (IOException ex) {
                        readListener.onError(ex);
                        throw ex;
                    }
                }
                return i;
            } else {
                return -1;
            }
        }

        @Override
        public int available() throws IOException {
            return (myBytes.length-lastIndexRetrieved-1);
        }

        @Override
        public void close() throws IOException {
            lastIndexRetrieved = myBytes.length-1;
            myBytes = null;
        }
    }
}

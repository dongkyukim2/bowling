package com.dk.project.post.retrofit;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ProgressRequestBody extends RequestBody {


    private final byte[] mBytes;
    private final File mFile;
    private ProgressListener mListener;
    private final long mLenght;
    private final MediaType mMediaType;

    private static final int DEFAULT_BUFFER_SIZE = (1024 * 1024 * 1) / 2; // 0.5MB

    // writeTo가 두번 호출되서 예외처리, 이유는 모름
    private int tempCount = 0;

    private ProgressRequestBody(final byte[] bytes, final File file, long lenght, MediaType mediaType, ProgressListener listener) {
        mBytes = bytes;
        mFile = file;
        mLenght = lenght;
        mMediaType = mediaType;
        mListener = listener;
    }

    /**
     * Send byte array.
     */
    public ProgressRequestBody(final byte[] bytes, final MediaType mediaType, final ProgressListener listener) {
        this(bytes, null, bytes.length, mediaType, listener);
    }

    /**
     * Send file.
     */
    public ProgressRequestBody(final File file, final MediaType mediaType, final ProgressListener listener) {
        this(null, file, file.length(), mediaType, listener);
    }

    @Override
    public MediaType contentType() {
        return mMediaType;
    }

    @Override
    public long contentLength() {
        return mLenght;
    }

    @Override
    public synchronized void writeTo(BufferedSink sink) throws IOException {
        if (tempCount == 0) {
            tempCount++;
            return;
        }
        InputStream is = createInputStream();
        try {
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            long uploaded = 0;
            int read;
            while ((read = is.read(buffer)) != -1) {
                sink.write(buffer, 0, read);
                uploaded += read;
                mListener.onRequestProgress(uploaded, mLenght);

            }
        } finally {
            closeInputStream(is);
            is.close();
        }
    }

    private void closeInputStream(InputStream is) {
        try {
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private InputStream createInputStream() throws FileNotFoundException {
        if (mBytes != null) {
            return new ByteArrayInputStream(mBytes);
        } else if (mFile != null) {
            return new FileInputStream(mFile);
        }
        throw new IllegalArgumentException("File and bytes are null");
    }

    public interface ProgressListener {

        void onUploadStart(String fileName);

        void onRequestProgress(long bytesWritten, long contentLength);

        void onUploadEnd(String fileName);
    }
}

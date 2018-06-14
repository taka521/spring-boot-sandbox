package org._521taka.multipart.streaming.filter;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 読み込んだデータサイズの合計をチェックするフィルター
 */
public class SizeCheckInputStreamFilter extends FilterInputStream {

    /** 合計サイズ */
    private long amount = 0;

    /** 上限サイズ */
    private long limitSize;

    /**
     * Creates a <code>FilterInputStream</code>
     * by assigning the  argument <code>in</code>
     * to the field <code>this.in</code> so as
     * to remember it for later use.
     *
     * @param in the underlying input stream, or <code>null</code> if
     *           this instance is to be created without an underlying stream.
     */
    private SizeCheckInputStreamFilter(final InputStream in) {
        super(in);
    }

    public SizeCheckInputStreamFilter(final InputStream in, final long limitSize) {
        super(in);
        this.limitSize = limitSize;
    }

    @Override
    public int read() throws IOException {
        return this.check(super.read());
    }

    @Override
    public int read(final byte[] b) throws IOException {
        return this.check(super.read(b));
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        return this.check(super.read(b, off, len));
    }

    /**
     * 合計サイズを取得します。
     *
     * @return 現在の合計サイズ
     */
    public long getAmount() {
        return this.amount;
    }

    /**
     * 合計サイズのチェックを行います。<br/>
     * 合計サイズが上限を超える場合{@link IOException}がスローされます。
     *
     * @param readSize 読み込んだデータサイズ
     *
     * @return 読み込んだサイズ
     *
     * @throws IOException 上限サイズを超えた場合
     */
    private int check(final int readSize) throws IOException {
        this.amount += readSize;
        if (amount > this.limitSize) throw new IOException("read size, limit over.");
        return readSize;
    }
}

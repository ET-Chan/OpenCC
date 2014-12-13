package opencc;

import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import java.nio.charset.Charset;

/**
 * Created by ET on 12/12/2014.
 */
public class OpenCCConverter implements AutoCloseable {
    static{
        Native.register("opencc");
    }
    private native Pointer opencc_open(String configFileName);
    private native int opencc_close(Pointer opencc);
    private native int opencc_convert_utf8_to_buffer(Pointer opencc,
                                                    String input,
                                                    int length,
                                                    byte[] output
    );
    Pointer p;
    public OpenCCConverter(String configFileName){
        p = opencc_open(configFileName);
    }
    public String convert(String input){
        int length = input.getBytes().length;
        byte[] output = new byte[length];
        opencc_convert_utf8_to_buffer(p,input,length,output);
        return new String(output, Charset.forName("UTF-8"));
    }
    @Override
    public void close() throws LastErrorException {
        opencc_close(p);
    }

    public static void main(String[] args) {
        OpenCCConverter c = new OpenCCConverter("s2t.json");
        System.out.println(c.convert("正簡轉換在Java。"));
        c.close();
    }
}

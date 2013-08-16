package com.maksym.ABSPager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Utils {
	public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
	
	/*
	 * Below function is used to Convert an INPUTSTREAM into the STRING
	 */
	public static String iStream_to_String(InputStream is1)
	{
			 BufferedReader rd = new BufferedReader(new InputStreamReader(is1), 4096);
			 String line;
			 StringBuilder sb =  new StringBuilder();
			 try {
				while ((line = rd.readLine()) != null) {
				 		sb.append(line);
				 }
				 rd.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 String contentOfMyInputStream = sb.toString();
			 return contentOfMyInputStream;
	}
}
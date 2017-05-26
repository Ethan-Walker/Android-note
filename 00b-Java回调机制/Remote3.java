package callback3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Remote3 {
	public static void readBigFile(String path,MyCallBack3 callBack3){
		new Thread( new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				InputStream reader = null;
				StringBuilder sb = null;
				try {
					reader = new FileInputStream(new File("callback.mp3"));
					byte[] bys = new byte[1024];
					sb= new StringBuilder();
					int len = 0;
					while((len=reader.read(bys))!=-1){
						sb.append(new String(bys));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					callBack3.onError("³öÏÖ´íÎó£¡");
				}finally{
					if(reader!=null){
						try {
							reader.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(sb!=null){
						callBack3.onFinish(sb.toString());
					}
					
				}
			}
		}).start();
			
	
	}
}

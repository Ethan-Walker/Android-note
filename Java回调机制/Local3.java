package callback3;

public class Local3 {
	public static void main(String[] args) {
		Remote3.readBigFile("callback.mp3", new MyCallBack3() {
			
			@Override
			public void onFinish(String data) {
				// TODO Auto-generated method stub
				System.out.println("�õ���ȡ���������Ϊ:"+data.length());
				
			}
			
			@Override
			public void onError(String err) {
				// TODO Auto-generated method stub
				System.out.println(err);
			}
		});
	}
}

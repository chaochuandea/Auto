
Auto原理说明
===== 
整体说明
![Test XIB](http://img.hb.aicdn.com/37a8f2e41cdb95aa8656c1a22da539b3e0662a577b0c-eBu3FH_fw580w)
下载过程说明
![Test XIB](http://img.hb.aicdn.com/aa2ae742f126158355b18ea6791cda72e465f631237b8-RzyYcA_fw580)
上传过程说明
![Test XIB](http://img.hb.aicdn.com/39b6887be17de90a22f17daa493c4cbff8435ed5e3f8-DMOKu5_fw580)
Auto用法说明
===== 
（1）声明Model层文件（UserInfo.java）
其中的@AURLVerable是指明那些字符串是变量;
其中@AModel里面的参数分别是：
            isDefaultToView----是否不用指定AModelField也会自动绑定View
            uploadUrl---上传地址
            loadUrl---下载地址（返回的是JSON，也可以是XML但是要用户自己实现ARemoteConverter接口）
其中@AData是指他是数据，绑定Activity中的非View变量
@AModelField是指定绑定View变量的ID，是否上传，上传变量的Key等等

```java
@AURLVerable(urlverable={"@veriable1","@veriable2"})
@AModel(isDefaultToView=true,uploadUrl="http://www.***.com/user.do?Act=userInfoEdit&sessionUid=@veriable1&sessionKey=@veriable2",loadUrl="http://www.***.com/user.do?Act=userInfo&sessionUid=@veriable1&sessionKey=@veriable2")
public class UserInfo {
	private String uid;
	@AData
	private String tel;
	private String company_area;
	@AModelField(viewid=R.id.bankProvice)
	private String bankProvince;
	private String loginname;
	private String company_name;
	private String userLogo;
	private String description;
	private String accountAddress;
	@AModelField(viewid=R.id.companey_tel)
	private String company_tel;
	private String taobaoBankCard;
	private String company_fax;
	private String company_city;
	
	private String bankCard;
	private String dat;
	private String company_description;
	@AModelField(viewid=R.id.accountNmae)
	private String accountName;
	private String nick;
	private String useremail;
	private String realname;
	private String company_province;
	private String mobile;
	//...setter and getter method
	}
``` 
（2）声明Layout
```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android" android:layout_width="match_parent" android:layout_height="wrap_content" >
    <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:orientation="vertical" >
        <Button android:id="@+id/sure" android:text="提交" android:layout_width="match_parent" android:layout_height="wrap_content"/>
        <EditText android:id="@+id/uid" android:layout_width="match_parent" android:layout_height="wrap_content" />
        <EditText android:id="@+id/tel" android:layout_width="match_parent" android:layout_height="wrap_content" />
        <EditText android:id="@+id/company_area" android:layout_width="match_parent" android:layout_height="wrap_content" />
        <EditText android:id="@+id/bankProvice" android:layout_width="match_parent" android:layout_height="wrap_content" />
        <EditText android:id="@+id/loginname" android:layout_width="match_parent" android:layout_height="wrap_content" />
        <EditText android:id="@+id/company_name" android:layout_width="match_parent" android:layout_height="wrap_content" />
        <EditText android:id="@+id/userLogo" android:layout_width="match_parent" android:layout_height="wrap_content" />
        <EditText android:id="@+id/description" android:layout_width="match_parent" android:layout_height="wrap_content" />
        <EditText android:id="@+id/accountAddress" android:layout_width="match_parent" android:layout_height="wrap_content" />
        <EditText android:id="@+id/companey_tel" android:layout_width="match_parent" android:layout_height="wrap_content" />
        <EditText android:id="@+id/taobaoBankCard" android:layout_width="match_parent" android:layout_height="wrap_content" />
        <EditText android:id="@+id/company_fax" android:layout_width="match_parent" android:layout_height="wrap_content" />
        <EditText android:id="@+id/company_city" android:layout_width="match_parent" android:layout_height="wrap_content" />
        <EditText android:id="@+id/bankCard" android:layout_width="match_parent" android:layout_height="wrap_content" />
        <EditText android:id="@+id/dat" android:layout_width="match_parent" android:layout_height="wrap_content" />
        <EditText android:id="@+id/company_description" android:layout_width="match_parent" android:layout_height="wrap_content" />
        <EditText android:id="@+id/accountNmae" android:layout_width="match_parent" android:layout_height="wrap_content" />
        <EditText android:id="@+id/nick" android:layout_width="match_parent" android:layout_height="wrap_content" />
        <EditText android:id="@+id/useremail" android:layout_width="match_parent" android:layout_height="wrap_content" />
        <EditText android:id="@+id/realname" android:layout_width="match_parent" android:layout_height="wrap_content" />
        <EditText android:id="@+id/company_province" android:layout_width="match_parent" android:layout_height="wrap_content" />
        <EditText android:id="@+id/mobile" android:layout_width="match_parent" android:layout_height="wrap_content" />
    </LinearLayout>
</ScrollView>
``` 
(3)Activity.JAVA
```java
public class MainActivity extends Activity {
	Auto a;
	String tel;//这是一个被Model层指定的AData数据
	@AView
	EditText uid,company_area,bankProvice,loginname,company_name,userLogo,description;
	
	@AView(id=R.id.tel)
	EditText tel_;
	
	@AView
	EditText accountAddress,companey_tel,taobaoBankCard,company_fax,company_city,bankCard,dat,company_description;
	
	@AView
	EditText accountNmae,nick,useremail,realname,company_province,mobile;
	
	@OnClick(R.id.uid)
	void textViewClick(View view){
		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
		startActivity(intent);
	}
	@AfterModelToActivity//这是第一种添加监听器的方法
	void update(){
		useremail.setText("这是模型层数据注入到Activity完成后设置的值");
		tel_.setText(tel);
		
	}
	@OnUpLoadStart
	void kaishishangchuan(){
		
	}
	
	@OnClick(R.id.sure)
	void toupload(View view){
		a.autoSync();
	}
	@AfterActivityToModel
	void activitytomodel(){
		System.out.println(a.getCurrentRequestParams().toString());
		Toast.makeText(getApplicationContext(), a.getCurrentRequestParams().toString(), Toast.LENGTH_SHORT).show();
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ModelUrlUtils.setUrlVerable("@veriable1", "2319965864");
        ModelUrlUtils.setUrlVerable("@veriable2", "0e30c1d502e506bf226fec1c8629b0b4");
        final UserInfo model = new UserInfo();
        a = new Auto( this,model);
        a.injectView();
        a.autoLoad();
    }
}
```


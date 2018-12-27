package controller;

import java.net.URLDecoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;

import model.Movie;
import model.Review;
import model.User;

public class UserController extends Controller {
	
	public void index() {
		render("firstPage.jsp");
	}
	//触发超链接事件，映射到登录页面
	public void login() {                         
		render("login.jsp");
	}
	//表单提交，验证用户登录
	public void submitL() {
		String name = getPara("name");
		String password = getPara("password");
		User user = User.dao.findFirst("select * from user where name=?",name);
		if(user!=null) {
			if(password.equals(user.getStr("password"))) {
				//强setAttribute较之setAttr
				getSession().setAttribute("name", user.getStr("name"));
				System.out.println("登录成功");
				render("/user/show.jsp");
			}else {
				System.out.println("密码不正确，请重新登录");
				//问题：会把之前输入的清空了。
				redirect("/user/login");
			}
		}else {
			System.out.println("账号不存在，请重新登录");
			redirect("/user/login");
		}
	}
	//学生登录之后的首页6
	public void show() {
		render("show.jsp");
	}
	
	//映射到注册页面
	public void register() {
		render("register.jsp");
	}
	//render和redirect的区别：render是渲染到当前目录下的URL。redirect是从项目跳转到项目根目录下URL
	//后端验证判断，只需判断用户名是否重复即可，无需密码
	public void submitR() {
		String name = getPara("user.name");
		User user1 = User.dao.findFirst("select * from user where name = ?",name);
		if(user1 == null) {
			User user2 = getModel(User.class,"user");
			user2.save();
			System.out.println("注册成功!");
			render("firstPage.jsp");
		}else {
			System.out.println("您输入的用户名已经存在！请重新注册!");
			redirect("/user/register");
		}
	}
	/*
	 *搜索框的实现 (firstPage是未登录的，而show是登录后的)927
	 */
	public void searchM() {
		//name用于判断是否登录。0未登录
		String Sname = URLDecoder.decode(getPara(0));
		//System.out.println(Sname);
		//也可能要URL解码，真的需要解码
		String input = getPara("inputSearch");
		//System.out.println(input);
		String dealInput = "%"+input+"%";
		Movie Smovie = Movie.dao.findFirst("select * from movie where name like ?",dealInput); 
//未检索到输入内容相关的电影。。这个有问题928
		if(Smovie == null) {
			System.out.println("未查到你要的电影名！");
			render("firstPage.jsp");
		}
		setAttr("movie", Smovie);
		//未登录情况，修改推荐(未检测)927
		if(Sname.equals("0")) {
			String types = Smovie.getStr("type");
			String[] type = types.split("/");
			String afterType = "%"+type[0]+"%";
			List<Movie> reMovies = Movie.dao.find("select * from movie where type like ? limit 0,6",afterType);
			System.out.println("得到数据"+ reMovies.size()+"个");
			setAttr("reMovies",reMovies);
			//这个代码也很关键，不然没有推荐.没用，为什么？928
			//setAttr("name",null);
//这两段代码有什么本质区别？？？为啥第一个不能把name设空，第二个可以？928
			getSession().setAttribute("name", null);
			render("detail.jsp");
		}
		//登录情况下
		else {
			//先获取评分，需要Sname（已知）和mid（要求）
			int mid = Smovie.getInt("id");
			Review Sreview = Review.dao.findFirst("select * from review where username = ? and mid = ?",Sname,mid);
			if(Sreview == null) {
				setAttr("score",null);
			}else {
				setAttr("score",Float.parseFloat(Sreview.getStr("myreview")));
			}
			List<Review> myReview1 = Review.dao.find("select * from review where username = ? limit 0,2",Sname);
	//这里也要考虑当前用户的评价电影数量
			if(myReview1.size() < 2) {
				String types = Smovie.getStr("type");
				String[] type = types.split("/");
				String afterType = "%"+type[0]+"%";
				List<Movie> reMovies = Movie.dao.find("select * from movie where type like ? limit 0,6",afterType);
				System.out.println("得到数据"+ reMovies.size()+"个");
				//这里要注意保存的参数
				setAttr("similarMovies",reMovies);
				render("detail.jsp");
			}
			//当前用户的两个电影评分
			if(myReview1.size() == 2) {
				float score1 = myReview1.get(0).getFloat("myreview");
				float score2 = myReview1.get(1).getFloat("myreview");
				int mid1 = myReview1.get(0).getInt("mid");
				int mid2 = myReview1.get(1).getInt("mid");
				//获取条件下一个字段的所有值（列）
				List<String> name1 = Db.query("select username from review where mid =?",mid1);
				List<String> name2 = Db.query("select username from review where mid =?",mid2);
				//similar中存放对这两个电影都有评价的用户和当前用户喜好相似程度（用欧几里得距离表示）
				HashMap<String,Double> similar = new HashMap<String,Double>();
				Review Review1 = new Review();
				Review Review2 = new Review();
				float myreview1 = 0.0f;
				float myreview2 = 0.0f;
				double sum = 0.0;
				double similarity = 0.0;
				double temp = 0.0;
				Boolean flag = true;
				for(int i =0; i<name1.size(); i++) {
					flag = true;
					for(int j=0;flag && j<name2.size(); j++) {
						if(name1.get(i).equals(name2.get(j))) {
							//每个i,只要匹配一个j就可以退出j循环了
							flag = false;
							Review1 = Review.dao.findFirst("select * from review where username = ? and mid = ?",name1.get(i),mid1);
							Review2 = Review.dao.findFirst("select * from review where username = ? and mid = ?",name2.get(i),mid2);
							myreview1 = Float.parseFloat(Review1.getStr("myreview"));
							myreview2 = Float.parseFloat(Review2.getStr("myreview"));
							sum = (myreview1-score1)*(myreview1-score1)+(myreview2-score2)*(myreview2-score2);
							similarity = Math.sqrt(sum);
							similar.put(name1.get(i),similarity);
							//允许第一次不验证执行（用简单的算法，去代替花里胡哨的hashmap技巧（还麻烦））
							//去掉绝对的0.0,假设不可能另一个用户对两个电影和你有一样的评价
							//获取到最小的value值：temp(方法：temp=0.0必须修改;similarity=0.0忽视掉)
							if(temp == 0.0 || (temp > similarity && similarity != 0.0)) {
								temp = similarity;
							}
						}
					}
				}
				//这里必要要对最相似的用户，做一个判断。看是不是真的最相似。最小相似值大于2.0。视为找不到，然后推荐最热度reMovies 928未实现
//				if(temp > 2.0) {
//					
//				}else {
//					
//				}
				//根据value值获取到对应的一个key值
				String key = null;
				for(String getKey : similar.keySet()) {
					if(similar.get(getKey).equals(temp)) {
						key = getKey;
					}
				}
				//根据最相似的用户名，找到他最喜欢的电影推荐给当前用户
				List<Integer> similarMids = Db.query("select mid from review where username = ? and myreview > 7.5 limit 0,6",key);
				List<Movie> similarMovies = new ArrayList<Movie>();
				for(int l=0; l<similarMids.size(); l++) {
					similarMovies.add(Movie.dao.findById(similarMids.get(l)));
				}
				System.out.println("得到数据"+ similarMovies.size()+"个");
				setAttr("similarMovies",similarMovies);
				
				render("detail.jsp");
			}
		}
		
	}
	
	/*
	 * 920问题：可以实现做成一个action的，但是数据库的数据传值出现乱码。。。后期再做
	 * 925解决：一定要用占位符。不说防止sql注入，但就乱码问题而言，要相当的方便 
	 * 占位符和%问题也一起解决了
	 */
	public void movieList() {
		int index = getParaToInt(0);
		String type = "";
		switch(index) {
		case 0: type = "喜剧";break;
		case 1: type = "科幻";break;
		case 2: type = "文艺";break;
		case 3: type = "剧情";break;
		case 4: type = "动作";break;
		case 5: type = "励志";break;
		default : type = "喜剧";
		}
		String type1 = "%"+type+"%";
		List<Movie> movies = Movie.dao.find("select * from movie where type like ? limit 0,20",type1);
		setAttr("movies",movies);
//		//得到当前所属的类型：第一种方法
//		String[] type2 = movies.get(0).getStr("type").split("/");
//		setAttr("type",type2[0]);
		//得到当前所属的类型：第二种方法（url参数传递）。。不是，改了
		setAttr("type",type);
		
		System.out.println("得到数据"+ movies.size()+"个");
		render("movie.jsp");
	}
	/*
	 * 页面详细设计(包括评分和未登录下的推荐)
	 */
	public void detail() {
		//0,1是根据传参顺序而来
		/*
		 * 评分
		 */
		Integer id = getParaToInt(0);
		setAttr("movie",Movie.dao.findById(id));
		//空还是不空->是否登录..这里需要URL地址的解码925
		String name = getPara(1);
		String username = "";
		//decode会报空指针异常
		if(name == null) {
			username = name;
		}else {
			username = URLDecoder.decode(name);
		}
		/*
		 * 其实这段代码，要放在username！=null中的，现在先不改-------------------要改动927
		 */
		//取数据库中这个人对这个电影的评分，我是保证每个都只有一条记录的。但是强制我用List,我也没办法.....findFirst还不错920
		Review review = Review.dao.findFirst("select * from review where username = ? and mid = ?",username,id);
		//这里不判断，在页面里面判断，来进行显示...
		if(review == null) {
			setAttr("score",null);
		//空指针异常，就是我没有得到真正的评分数据
		}else {
			setAttr("score",Float.parseFloat(review.getStr("myreview")));
		}
		/*
		 * 推荐925
		 */
		//用户未登录下的推荐
		if(username == null) {
			//得到当前电影的类型，用于给未登录的用户推荐相同类型的电影.如果有的话，其实就是可以封装成findByType()..也可以不这样说
			Movie movie = Movie.dao.findById(id);
			//这样求得当前电影具体所属的类型，但对于当下的项目没必要，只是写个接口，供以后有兴趣研究，所以这里我只取电影类型的第一个，作为推荐电影类型的依据
			String types = movie.getStr("type");
			String[] type = types.split("/");
			//924下午出现问题：占位符和%连用的问题（无法识别%?%），
			//925必须把%和数据打包在一起作为占位符来解决。（这样其实上面根据类型来的查找也可以简写了）
			String afterType = "%"+type[0]+"%";
			//避免推荐重复的电影：取6条记录，显示5条记录。
			//未解决：不能只显示5条。不知如何在页面中控制显示的个数，而不是用记录索引
			List<Movie> reMovies = Movie.dao.find("select * from movie where type like ? limit 0,6",afterType);
			System.out.println("得到数据"+ reMovies.size()+"个");
			setAttr("reMovies",reMovies);
			
		}
		else {
			//用户登陆下的推荐926
			//首先找到用户有无对当前电影的评分（在页面里面计算，这里不写。权当此时用户已经对这个电影有评价）
			//如何计算欧几里得距离（随机找当前用户的两个电影评价，换算成坐标值。找对这两个电影评价和其相近评价的另一个用户。评价高（大于8）的其他电影，推荐给当前用户。这些信息全都渲染在detail中）
			List<Review> myReview1 = Review.dao.find("select * from review where username = ? limit 0,2",username);
			//当前用户的两个电影评分
			if(myReview1.size() == 2) {
				float score1 = myReview1.get(0).getFloat("myreview");
				float score2 = myReview1.get(1).getFloat("myreview");
				//System.out.println("评分1："+score1+"，评分2："+score2);
				int mid1 = myReview1.get(0).getInt("mid");
				int mid2 = myReview1.get(1).getInt("mid");
				//System.out.println("电影id1："+mid1+"，电影id2："+mid2);
				//获取条件下一个字段的所有值（列）
				List<String> name1 = Db.query("select username from review where mid =?",mid1);
//				for (int a = 0; a < name1.size(); a++) {
//					System.out.println("名字1，"+name1.get(a));
//				}
				List<String> name2 = Db.query("select username from review where mid =?",mid2);
//				for (int b = 0; b < name2.size(); b++) {
//					System.out.println("名字2，"+name2.get(b));
//				}
				//similar中存放对这两个电影都有评价的用户和当前用户喜好相似程度（用欧几里得距离表示）
				HashMap<String,Double> similar = new HashMap<String,Double>();
				Review Review1 = new Review();
				Review Review2 = new Review();
				float myreview1 = 0.0f;
				float myreview2 = 0.0f;
				double sum = 0.0;
				double similarity = 0.0;
				double temp = 0.0;
				Boolean flag = true;
				for(int i =0; i<name1.size(); i++) {
					flag = true;
					for(int j=0;flag && j<name2.size(); j++) {
						if(name1.get(i).equals(name2.get(j))) {
							//每个i,只要匹配一个j就可以退出j循环了
							flag = false;
							Review1 = Review.dao.findFirst("select * from review where username = ? and mid = ?",name1.get(i),mid1);
							Review2 = Review.dao.findFirst("select * from review where username = ? and mid = ?",name2.get(i),mid2);
							myreview1 = Float.parseFloat(Review1.getStr("myreview"));
							myreview2 = Float.parseFloat(Review2.getStr("myreview"));
							//System.out.println("1评分1："+myreview1+"，2评分2："+myreview2);
							sum = (myreview1-score1)*(myreview1-score1)+(myreview2-score2)*(myreview2-score2);
							similarity = Math.sqrt(sum);
							//System.out.println("相似度："+similarity);
							similar.put(name1.get(i),similarity);
							//允许第一次不验证执行（用简单的算法，去代替花里胡哨的hashmap技巧（还麻烦））
							//去掉绝对的0.0,假设不可能另一个用户对两个电影和你有一样的评价
							//获取到最小的value值(方法：temp=0.0必须修改;similarity=0.0忽视掉)
							if(temp == 0.0 || (temp > similarity && similarity != 0.0)) {
								temp = similarity;
							}
						}
					}
				}
				//System.out.println("hashmap的键值对："+similar);
				//System.out.println(temp);
				//根据value值获取到对应的一个key值
				String key = null;
				for(String getKey : similar.keySet()) {
					if(similar.get(getKey).equals(temp)) {
						key = getKey;
					}
				}
				//System.out.println(key);
				//根据最相似的用户名，找到他最喜欢的电影推荐给当前用户
				List<Integer> similarMids = Db.query("select mid from review where username = ? and myreview > 7.5 limit 0,6",key);
				List<Movie> similarMovies = new ArrayList<Movie>();
				for(int l=0; l<similarMids.size(); l++) {
					similarMovies.add(Movie.dao.findById(similarMids.get(l)));
					//System.out.println(similarMids.get(l));
				}
				System.out.println("得到数据"+ similarMovies.size()+"个");
//				for (int d = 0; d < similarMovies.size(); d++) {
//					System.out.println(similarMovies.get(d).getInt("id"));
//				}
				setAttr("similarMovies",similarMovies);
			}
		}
		render("detail.jsp");
	}
	/*
	 * 对电影打分(添加数据)
	 */
	public void addReview() {
		Review review = getModel(Review.class,"review");
		review.save();
		//需要重新保存会话内容，为什么。建立会话的持续时间是什么。我的猜测就是重新render(渲染)会导致会话的断开(也就是之前保存在会话中的数据都清空了)很烦925
		int mid = getParaToInt("review.mid");
		//通过这里就可以看出，其实对于每一张表，只要获得每一个ID就可以找到对应的其他字段，用session保存就OK了。没之前想的那么复杂
		Movie movie = Movie.dao.findById(mid);
		setAttr("movie",movie);
		String username = getPara("review.username");
		setAttr("name", username);
		String score = getPara("review.myreview");
		setAttr("score",score);
		/*
		 * （也重复）即使是重新评分，也需要进行重新推荐。。能不能找到一个简便的方法926
		 */
	//修改顺序，考虑到当用户评价的电影不足两条时，推荐热搜。未完善928
		List<Review> myReview1 = Review.dao.find("select * from review where username = ? limit 0,2",username);
		if(myReview1.size() < 2) {
			String types = movie.getStr("type");
			String[] type = types.split("/");
			String afterType = "%"+type[0]+"%";
			List<Movie> reMovies = Movie.dao.find("select * from movie where type like ? limit 0,5",afterType);
			System.out.println("得到数据"+ reMovies.size()+"个");
			
			//这种情况是，用户存在，但当前对电影的评价小于2条
			setAttr("similarMovies",reMovies);
		}
		//当前用户的两个电影评分
		if(myReview1.size() >= 2) {
			float score1 = myReview1.get(0).getFloat("myreview");
			float score2 = myReview1.get(1).getFloat("myreview");
			//System.out.println("评分1："+score1+"，评分2："+score2);
			int mid1 = myReview1.get(0).getInt("mid");
			int mid2 = myReview1.get(1).getInt("mid");
			//System.out.println("电影1："+mid1+"，电影2："+mid2);
			//获取条件下一个字段的所有值（列）
			List<String> name1 = Db.query("select username from review where mid =?",mid1);
//			for (int a = 0; a < name1.size(); a++) {
//				System.out.println("名字1，"+name1.get(a));
//			}
			List<String> name2 = Db.query("select username from review where mid =?",mid2);
//			for (int b = 0; b < name2.size(); b++) {
//				System.out.println("名字2，"+name2.get(b));
//			}
			//similar中存放对这两个电影都有评价的用户和当前用户喜好相似程度（用欧几里得距离表示）
			HashMap<String,Double> similar = new HashMap<String,Double>();
			Review Review1 = new Review();
			Review Review2 = new Review();
			float myreview1 = 0.0f;
			float myreview2 = 0.0f;
			double sum = 0.0;
			double similarity = 0.0;
			double temp = 0.0;
			Boolean flag = true;
			for(int i =0; i<name1.size(); i++) {
				flag = true;
				for(int j=0; flag && j<name2.size(); j++) {
					if(name1.get(i).equals(name2.get(j))) {
						flag = false;
						Review1 = Review.dao.findFirst("select * from review where username = ? and mid = ?",name1.get(i),mid1);
						Review2 = Review.dao.findFirst("select * from review where username = ? and mid = ?",name2.get(i),mid2);
						myreview1 = Float.parseFloat(Review1.getStr("myreview"));
						myreview2 = Float.parseFloat(Review2.getStr("myreview"));
						//System.out.println("1评分1："+myreview1+"，2评分2："+myreview2);
						sum = (myreview1-score1)*(myreview1-score1)+(myreview2-score2)*(myreview2-score2);
						similarity = Math.sqrt(sum);
						//System.out.println("相似度："+similarity);
						similar.put(name1.get(i),similarity);
						//允许第一次不验证执行（用简单的算法，去代替花里胡哨的hashmap技巧（还麻烦））
						//去掉绝对的0.0,假设不可能另一个用户对两个电影和你有一样的评价
						//获取到最小的value值(方法：temp=0.0必须修改;similarity=0.0忽视掉)
						if(temp == 0.0 || (temp > similarity && similarity != 0.0)) {
							temp = similarity;
						}
					}
				}
			}
			//System.out.println(temp);
			//根据value值获取到对应的一个key值
			String key = null;
			for(String getKey : similar.keySet()) {
				if(similar.get(getKey).equals(temp)) {
					key = getKey;
				}
			}
			//根据最相似的用户名，找到他最喜欢的电影推荐给当前用户
			List<Integer> similarMids = Db.query("select mid from review where username = ? and myreview > 7.5 limit 0,5",key);
			List<Movie> similarMovies = new ArrayList<Movie>();
			for(int l=0; l<similarMids.size(); l++) {
				similarMovies.add(Movie.dao.findById(similarMids.get(l)));
				//System.out.println(similarMids.get(l));
			}
			System.out.println("得到数据"+ similarMovies.size()+"个");
//			for (int d = 0; d < similarMovies.size(); d++) {
//				System.out.println(similarMovies.get(d).getInt("id"));
//			}
			setAttr("similarMovies",similarMovies);
		}
		render("/user/detail.jsp");
	}
	/*
	 * 修改数据(通过用户id和电影id找到对应的流水号，也就是对应的字符序号)
	 * 定位到唯一的流水号之后，就能进行更新
	 */
	public void updateReview() {
		int mid = getParaToInt("review.mid");
		//这里也是，重复调用。有点麻烦。render的后果
		Movie movie = Movie.dao.findById(mid);
		setAttr("movie",movie);
		String username = getPara("review.username");
		setAttr("name", username);
		
		String score = getPara("review.myreview");
		setAttr("score",score);
		
//		Review review = getModel(Review.class,"review");
//		//能不能不根据提供的API进行update，要自定义update语句。如何获取数据库的连接，要不要熟悉阿里的数据库连接池、
//		review.update();
		//如何自定义自己的update语句924
		//不需要了，因为jfinal是Db+Record模式太强大了
		//评分不能空 927
		Db.update("update review set myreview = ? where mid = ? and username = ?",score,mid,username);
		
		/*
		 * （也重复）即使是重新评分，也需要进行重新推荐。。能不能找到一个简便的方法926
		 */
		List<Review> myReview1 = Review.dao.find("select * from review where username = ? limit 0,2",username);
		//当前用户的两个电影评分
		if(myReview1.size() >= 2) {
			float score1 = myReview1.get(0).getFloat("myreview");
			float score2 = myReview1.get(1).getFloat("myreview");
			//System.out.println("评分1："+score1+"，评分2："+score2);
			int mid1 = myReview1.get(0).getInt("mid");
			int mid2 = myReview1.get(1).getInt("mid");
			//System.out.println("电影1："+mid1+"，电影2："+mid2);
			//获取条件下一个字段的所有值（列）
			List<String> name1 = Db.query("select username from review where mid =?",mid1);
			List<String> name2 = Db.query("select username from review where mid =?",mid2);
			//similar中存放对这两个电影都有评价的用户和当前用户喜好相似程度（用欧几里得距离表示）
			HashMap<String,Double> similar = new HashMap<String,Double>();
			Review Review1 = new Review();
			Review Review2 = new Review();
			float myreview1 = 0.0f;
			float myreview2 = 0.0f;
			double sum = 0.0;
			double similarity = 0.0;
			double temp = 0.0;
			Boolean flag = true;
			for(int i =0; i<name1.size(); i++) {
				flag = true;
				for(int j=0; flag && j<name2.size(); j++) {
					if(name1.get(i).equals(name2.get(j))) {
						flag = false;
						Review1 = Review.dao.findFirst("select * from review where username = ? and mid = ?",name1.get(i),mid1);
						Review2 = Review.dao.findFirst("select * from review where username = ? and mid = ?",name2.get(i),mid2);
						myreview1 = Float.parseFloat(Review1.getStr("myreview"));
						myreview2 = Float.parseFloat(Review2.getStr("myreview"));
						//System.out.println("1评分1："+myreview1+"，2评分2："+myreview2);
						sum = (myreview1-score1)*(myreview1-score1)+(myreview2-score2)*(myreview2-score2);
						similarity = Math.sqrt(sum);
						//System.out.println("相似度："+similarity);
						similar.put(name1.get(i),similarity);
						//允许第一次不验证执行（用简单的算法，去代替花里胡哨的hashmap技巧（还麻烦））
						//去掉绝对的0.0,假设不可能另一个用户对两个电影和你有一样的评价
						//获取到最小的value值(方法：temp=0.0必须修改;similarity=0.0忽视掉)
						if(temp == 0.0 || (temp > similarity && similarity != 0.0)) {
							temp = similarity;
						}
					}
				}
			}
			//System.out.println(temp);
			//根据value值获取到对应的一个key值
			String key = null;
			for(String getKey : similar.keySet()) {
				if(similar.get(getKey).equals(temp)) {
					key = getKey;
				}
			}
			//根据最相似的用户名，找到他最喜欢的电影推荐给当前用户
			List<Integer> similarMids = Db.query("select mid from review where username = ? and myreview > 7.5 limit 0,5",key);
			List<Movie> similarMovies = new ArrayList<Movie>();
			for(int l=0; l<similarMids.size(); l++) {
				similarMovies.add(Movie.dao.findById(similarMids.get(l)));
				//System.out.println(similarMids.get(l));
			}
			System.out.println("得到数据"+ similarMovies.size()+"个");
//			for (int d = 0; d < similarMovies.size(); d++) {
//				System.out.println(similarMovies.get(d).getInt("id"));
//			}
			setAttr("similarMovies",similarMovies);
		}
		render("/user/detail.jsp");
	}
	
	/*
	 * 管理员访问用户列表页面
	 */
	public void list() {
		List<User> users = User.dao.find("select * from user");
		//用于list页面中遍历数据库user表
		setAttr("users", users);
		System.out.println("得到数据"+users.size()+"个");
		render("list.jsp");
	}
	/*
	 * 增加和修改
	 */
	public void add() {
		Integer id = getParaToInt(0);
		if(id!=null && id>0) {
			setAttr("user", User.dao.findById(id));
		}
		render("index.jsp");
	}
}
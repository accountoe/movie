package controller;

import java.util.List;

import com.jfinal.core.Controller;

import model.Movie;

public class IndexController extends Controller {
	public void index() {
		render("firstPage.jsp");
	}
	//IndexController和UserController的分离，不好做，改的太多927
	//处理方式：把所有的页面全部都放在user下，firstPage也是。这样不用根目录URL的来回切换
	public void searchM() {
		String name = getPara("index");
		System.out.println(name);
		String input = getPara("inputSearch");
		System.out.println(input);
		Movie Smovie = Movie.dao.findFirst("select * from movie where name = ?",input); 
		setAttr("movie", Smovie);
		
		String types = Smovie.getStr("type");
		String[] type = types.split("/");
		String afterType = "%"+type[0]+"%";
		List<Movie> reMovies = Movie.dao.find("select * from movie where type like ? limit 0,6",afterType);
		System.out.println("得到数据"+ reMovies.size()+"个");
		setAttr("reMovies",reMovies);
		render("/user/detail.jsp");
	}
}

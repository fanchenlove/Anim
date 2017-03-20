package com.fanchen.anim.retrofit.converter;

import android.text.TextUtils;

import com.fanchen.anim.entity.Anim;
import com.fanchen.anim.entity.AnimDetails;
import com.fanchen.anim.entity.AnimEpisode;
import com.fanchen.anim.entity.AnimItem;
import com.fanchen.anim.parser.Node;
import com.fanchen.anim.parser.annotation.AnimMethodType;
import com.fanchen.anim.util.LogUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fanchen on 2017/1/19.
 */
public class FengcheConverter extends BaseStringConverter{

    public FengcheConverter(AnimMethodType animMethodType) {
        super(animMethodType);
    }

    @Override
    protected Object convert(String str, AnimMethodType methodType) throws IOException {
        Node node = new Node(str);
        if(methodType == AnimMethodType.HOME){

        }else if(methodType == AnimMethodType.ITEMLIST){
            List<Anim> animList = new ArrayList<>();
            try {
                for (Node n : node.list("div#contrainer > div.imgs > ul > li")){
                    String cover = n.attr("a > img","src");
                    String title = n.attr("a > img", "alt");
                    String last = n.text("p:eq(2)");
                    String href = n.attr("a", "href");
                    String catid = "";
                    if(href.length() > 14){
                        catid = href.substring(9,href.length() - 5);
                    }
                    Anim anim = new Anim();
                    anim.setSource(Anim.FENGCHE);
                    anim.setCatid(catid);
                    anim.setCover(cover);
                    anim.setTitle(title);
                    anim.setLastEpisode(last);
                    animList.add(anim);
                }
                LogUtil.e(getClass(),new Gson().toJson(animList));
                return animList;
            }catch (Exception e){
                e.printStackTrace();
                throw new IOException("parser data error");
            }
        }else if(methodType == AnimMethodType.DETAILS){
            try {
                AnimDetails details = new AnimDetails();
                String cover = node.attr("div.area > div.fire.l > div.tpic.l > img","src");
                String title = node.text("div.area > div.fire.l > div.intro.r > div.alex > p");
                String last = node.text("div.area > div.fire.l > div.intro.r > div.alex > span:eq(1)");
                String states = node.text("div.area > div.fire.l > div.intro.r > div.alex > span:eq(2)");
                String author = node.text("div.area > div.fire.l > div.intro.r > div.alex > span:eq(3)");
                String type = node.text("div.area > div.fire.l > div.intro.r > div.alex > span:eq(5)");
                String intro = node.text("div.area > div.fire.l > div.info");
                details.setCover(cover);
                details.setTitle(title);
                details.setLastEpisode(last);
                details.setState(states);
                details.setAuthor(author);
                details.setType(type);
                details.setIntro(intro);
                details.setSource(Anim.FENGCHE);
                List<AnimEpisode> episodes = new ArrayList<>();
                details.setEpisodes(episodes);
                List<AnimItem> recommends = new ArrayList<>();
                details.setRecommends(recommends);
                Map<Integer,String> map = new HashMap<>();
                List<Node> menuList = node.list("div.area > div.fire.l > div.tabs > ul.menu0");
                for (int i = 0 ; i < menuList.size() ; i ++){
                    map.put(Integer.valueOf(i),menuList.get(i).text("li"));
                }
                List<Node> mainList = node.list("div.area > div.fire.l > div.tabs > div.main0");
                for (int i = 0 ; i < mainList.size() ; i ++){
                    String from = map.get(Integer.valueOf(i));
                    if(!TextUtils.isEmpty(from)){
                        for (Node n : mainList.get(i).list("div > ul > li")){
                            AnimEpisode episode = new AnimEpisode();
                            String episodeTitle = n.text("a");
                            episode.setSource(Anim.FENGCHE);
                            episode.setFrom(from);
                            String episodeUrl = n.attr("a", "href");
                            LogUtil.e(getClass(),episodeTitle);
                            if(episodeUrl.indexOf("-") != -1){
                                episodeUrl = episodeUrl.substring(episodeUrl.lastIndexOf("/") + 1 , episodeUrl.length() - 5);
                                String[] split = episodeUrl.split("-");
                                if(split.length == 2){
                                    episode.setCatid(split[0]);
                                    episode.setId(split[1]);
                                }else if(split.length == 3){
                                    episode.setCatid(split[0]);
                                    episode.setId(split[1] + "-" + split[2]);
                                }
                            }else{
                                String[] split = episodeUrl.split("/");
                                if(split.length > 2 ){
                                    String id = split[split.length - 1];
                                    String catid = split[split.length - 2];
                                    episode.setId(id);
                                    episode.setCatid(catid);
                                }
                            }
                            episodes.add(episode);
                        }
                    }
                }
                for (Node n : node.list("div.area > div.fire.l > div.imgs > ul > li")){
                    String itemCover = n.attr("a > img","src");
                    String itemTitle = n.attr("a > img", "alt");
                    String itemUrl = n.attr("a", "href");
                    String itemCatid = "";
                    if(itemUrl.length() > 14){
                        itemCatid = itemUrl.substring(9,itemUrl.length() - 5);
                    }
                    AnimItem item = new AnimItem();
                    item.setSource(Anim.FENGCHE);
                    item.setCover(itemCover);
                    item.setTitle(itemTitle);
                    item.setCatid(itemCatid);
                    recommends.add(item);
                }
                return details;
            }catch (Exception e){
                e.printStackTrace();
                throw new IOException("parser data error");
            }
        }else if(methodType == AnimMethodType.PLAYURL){

        }else if(methodType == AnimMethodType.SEARCH){

        }
        return null;
    }
}

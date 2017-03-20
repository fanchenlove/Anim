package com.fanchen.anim.retrofit.converter;

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
import java.util.List;


/**
 * Created by fanchen on 2017/1/10.
 */
public class DiliConverter extends BaseStringConverter {

    public DiliConverter(AnimMethodType animMethodType) {
        super(animMethodType);
    }

    @Override
    protected Object convert(String str, AnimMethodType methodType) throws IOException {
        Node node = new Node(str);
        if(methodType == AnimMethodType.HOME){

        }else if(methodType == AnimMethodType.ITEMLIST){
            List<Anim> animes = new ArrayList<>();
            try{
                for (Node n : node.list("div.anime_list > dl")){
                    String cover = n.attr("dt > a > img","src");
                    String url = n.attr("dt > a", "href");
                    String id = "";
                    if(url.length() > 8){
                        id = url.substring(7,url.length() - 1);
                    }
                    String title = n.text("dd > h3");
                    String author = n.text("dd > p:eq(9)");
                    String intro = n.text("dd > p:eq(10)");
                    String tags = n.text("dd > div.d_label:eq(6)");
                    String state = n.last("dd > p").text();
                    String type = n.text("dd > div.d_label:eq(2)");
                    String time = n.text("dd > div.d_label:eq(3)");
                    Anim anime = new Anim();
                    anime.setCover(cover);
                    anime.setTitle(title);
                    anime.setCatid(id);
                    anime.setTags(tags);
                    anime.setIntro(intro);
                    anime.setState(state);
                    anime.setAuthor(author);
                    anime.setType(type);
                    anime.setTime(time);
                    anime.setSource(Anim.DILIDLI);
                    animes.add(anime);
                }
            }catch (Exception e){
                e.printStackTrace();
                throw new IOException("parser data error");
            }
            return animes;
        }else if(methodType == AnimMethodType.DETAILS){
            try {
                AnimDetails details = new AnimDetails();
                String cover = node.attr("div.aside_cen2 > div.detail.con24.clear > dl > dt > img","src");
                String title = node.text("div.aside_cen2 > div.detail.con24.clear > dl > dd > h1");
                String type = node.text("div.aside_cen2 > div.detail.con24.clear > dl > dd > div.d_label:eq(5)");
                String time = node.text("div.aside_cen2 > div.detail.con24.clear > dl > dd > div.d_label:eq(6)");
                String author = node.text("div.aside_cen2 > div.detail.con24.clear > dl > dd > div.d_label2:eq(13)");
                String tags = node.text("div.aside_cen2 > div.detail.con24.clear > dl > dd > div.d_label:eq(9)");
                String stutas = node.text("div.aside_cen2 > div.detail.con24.clear > dl > dd > p:eq(7)");
                String intro = node.text("div.aside_cen2 > div.detail.con24.clear > dl > dd > div.d_label2:eq(16)");
                details.setCover(cover);
                details.setTitle(title);
                details.setTime(time);
                details.setTags(tags);
                details.setIntro(intro);
                details.setState(stutas);
                details.setAuthor(author);
                details.setType(type);
                List<AnimEpisode> episodes = new ArrayList<>();
                details.setEpisodes(episodes);
                for (Node n : node.list("div.swiper-wrapper.mb20 > div > ul.clear > li")){
                    AnimEpisode episode = new AnimEpisode();
                    String episodeTitle = n.text("a > em");
                    String episodeUrl = n.attr("a", "href");
                    String[] split = episodeUrl.split("/");
                    if(split.length > 2 ){
                        String id = split[split.length - 1];
                        String catid = split[split.length - 2];
                        episode.setId(id);
                        episode.setCatid(catid);
                    }
                    episode.setTitle(episodeTitle);
                    episode.setSource(Anim.DILIDLI);
                    episodes.add(episode);
                }
                List<AnimItem> anims = new ArrayList<>();
                details.setRecommends(anims);
                for (Node n : node.list("div.swiper-wrapper > div > ul.m_pic.clear > li")){
                    AnimItem item = new AnimItem();
                    String itemCover = n.attr("a > img","src");
                    String itemTitle = n.text("a > p");
                    String itemUrl = n.attr("a", "href");
                    String id = "";
                    if(itemUrl.length() > 8){
                        id = itemUrl.substring(7);
                    }
                    item.setSource(Anim.DILIDLI);
                    item.setTitle(itemTitle);
                    item.setCatid(id);
                    item.setCover(itemCover);
                    anims.add(item);
                }
                LogUtil.e(getClass(),new Gson().toJson(details));
                return details;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

}

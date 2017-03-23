package com.huliang.oschn.improve.bean;

import android.text.TextUtils;

import com.huliang.oschn.improve.bean.simple.Author;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huliang on 17/3/19.
 */
public class Tweet implements Serializable {
    private long id;
    private String content;
    private int appClient;
    private int commentCount;
    private int likeCount;
    private boolean liked;
    private String pubDate;
    private String href;
    private Author author;
    private Image[] images;
    private Statistics statistics;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAppClient() {
        return appClient;
    }

    public void setAppClient(int appClient) {
        this.appClient = appClient;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Image[] getImages() {
        return images;
    }

    public void setImages(Image[] images) {
        this.images = images;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public static class Statistics implements Serializable {
        private int comment;
        private int transmit;
        private int like;

        public int getComment() {
            return comment;
        }

        public void setComment(int comment) {
            this.comment = comment;
        }

        public int getTransmit() {
            return transmit;
        }

        public void setTransmit(int transmit) {
            this.transmit = transmit;
        }

        public int getLike() {
            return like;
        }

        public void setLike(int like) {
            this.like = like;
        }
    }

    public static class Image implements Serializable {
        private String thumb;
        private String href;
        private String name;
        private int type;
        private int w;
        private int h;

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getW() {
            return w;
        }

        public void setW(int w) {
            this.w = w;
        }

        public int getH() {
            return h;
        }

        public void setH(int h) {
            this.h = h;
        }

        public static boolean check(Image image) {
            return (image != null && !TextUtils.isEmpty(image.getThumb())
                    && !TextUtils.isEmpty(image.getHref()));
        }

        /**
         * 获取图片列表的urls
         *
         * @param images
         * @return
         */
        public static String[] getImagePaths(Image[] images) {
            if (images == null || images.length == 0) {
                return null;
            }
            List<String> paths = new ArrayList<>();
            for (Image image : images) {
                if (check(image)) {
                    paths.add(image.getHref());
                }
            }
            return paths.toArray(new String[paths.size()]);
        }
    }
}

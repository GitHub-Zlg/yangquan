package app.com.yangquan.bean;

import java.util.List;

public class TrendBean {


    /**
     * code : 0
     * msg : 获取数据成功
     * data : [{"img":[{"id":"1","img":"http://www.xiangyingchun.cn/upload/20200608/7099e2fec1743aba00e050d9c28bcfd0.jpg","pid":"1","is_del":"0"},{"id":"2","img":"http://www.xiangyingchun.cn/upload/20200608/637015c64f78f07ed116d5e4dc43d472.jpg","pid":"1","is_del":"0"},{"id":"3","img":"http://www.xiangyingchun.cn/upload/20200608/b3671d8d265c1aa9359b34f5f48d7ae7.jpg","pid":"1","is_del":"0"},{"id":"4","img":"http://www.xiangyingchun.cn/upload/20200608/710348c7cccf1a8a4bde4560980ee471.jpg","pid":"1","is_del":"0"},{"id":"5","img":"http://www.xiangyingchun.cn/upload/20200608/63b2c99e424557d4f53b665943dae38d.jpg","pid":"1","is_del":"0"},{"id":"6","img":"http://www.xiangyingchun.cn/upload/20200608/6de0f38c4defdf4988e436e77be89ab1.jpg","pid":"1","is_del":"0"}],"user_img":"http://www.xiangyingchun.cn/upload/20200608/f1baf9694acaae5478c07bea8b0ecea3.jpg","id":"1","uid":"106","time":"2020-06-08 07:51:01","content":"你好羊圈！","nickname":"德玛西亚"}]
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * img : [{"id":"1","img":"http://www.xiangyingchun.cn/upload/20200608/7099e2fec1743aba00e050d9c28bcfd0.jpg","pid":"1","is_del":"0"},{"id":"2","img":"http://www.xiangyingchun.cn/upload/20200608/637015c64f78f07ed116d5e4dc43d472.jpg","pid":"1","is_del":"0"},{"id":"3","img":"http://www.xiangyingchun.cn/upload/20200608/b3671d8d265c1aa9359b34f5f48d7ae7.jpg","pid":"1","is_del":"0"},{"id":"4","img":"http://www.xiangyingchun.cn/upload/20200608/710348c7cccf1a8a4bde4560980ee471.jpg","pid":"1","is_del":"0"},{"id":"5","img":"http://www.xiangyingchun.cn/upload/20200608/63b2c99e424557d4f53b665943dae38d.jpg","pid":"1","is_del":"0"},{"id":"6","img":"http://www.xiangyingchun.cn/upload/20200608/6de0f38c4defdf4988e436e77be89ab1.jpg","pid":"1","is_del":"0"}]
         * user_img : http://www.xiangyingchun.cn/upload/20200608/f1baf9694acaae5478c07bea8b0ecea3.jpg
         * id : 1
         * uid : 106
         * time : 2020-06-08 07:51:01
         * content : 你好羊圈！
         * nickname : 德玛西亚
         */

        private String user_img;
        private String id;
        private String uid;
        private String time;
        private String content;
        private String nickname;
        private List<ImgBean> img;

        public String getUser_img() {
            return user_img;
        }

        public void setUser_img(String user_img) {
            this.user_img = user_img;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public List<ImgBean> getImg() {
            return img;
        }

        public void setImg(List<ImgBean> img) {
            this.img = img;
        }

        public static class ImgBean {
            /**
             * id : 1
             * img : http://www.xiangyingchun.cn/upload/20200608/7099e2fec1743aba00e050d9c28bcfd0.jpg
             * pid : 1
             * is_del : 0
             */

            private String id;
            private String img;
            private String pid;
            private String is_del;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public String getIs_del() {
                return is_del;
            }

            public void setIs_del(String is_del) {
                this.is_del = is_del;
            }
        }
    }
}

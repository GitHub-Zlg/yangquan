package app.com.yangquan.bean;

import java.util.List;

public class BlindBean {

    /**
     * code : 0
     * msg : 获取数据成功
     * data : [{"id":"1","username":"13133133313","nickname":"","sex":"0","hangye":"","age":"0","user_img":"http://www.xiangyingchun.cn/upload/20200603/97029165109c96948d1c7edf1a7d55cd.jpg","autograph":"个人签名设置","constellation":"0","birthday":"0000-00-00"},{"id":"2","username":"13512511029","nickname":"","sex":"1","hangye":"","age":"0","user_img":"http://www.xiangyingchun.cn/upload/20200603/97029165109c96948d1c7edf1a7d55cd.jpg","autograph":" ","constellation":"0","birthday":"0000-00-00","home_img":"http://www.xiangyingchun.cn/upload/index/1.jpg"},{"id":"4","username":"13133137283","nickname":"","sex":"0","hangye":"","age":"0","user_img":"http://www.xiangyingchun.cn/upload/20200603/97029165109c96948d1c7edf1a7d55cd.jpg","autograph":" ","constellation":"0","birthday":"0000-00-00"},{"id":"6","username":"13133133314","nickname":"柴蔓菁","sex":"1","hangye":"企业主管","age":"19","user_img":"http://www.xiangyingchun.cn/upload/20200603/97029165109c96948d1c7edf1a7d55cd.jpg","autograph":"","constellation":"0","birthday":"0000-00-00","home_img":"http://www.xiangyingchun.cn/upload/index/1.jpg"},{"id":"7","username":"13133133315","nickname":"竺嘉荣","sex":"1","hangye":"经理人","age":"20","user_img":"http://www.xiangyingchun.cn/upload/20200603/97029165109c96948d1c7edf1a7d55cd.jpg","autograph":"","constellation":"0","birthday":"0000-00-00","home_img":"http://www.xiangyingchun.cn/upload/index/1.jpg"},{"id":"8","username":"13133133316","nickname":"无甘","sex":"1","hangye":"土木营造监工","age":"21","user_img":"http://www.xiangyingchun.cn/upload/20200603/97029165109c96948d1c7edf1a7d55cd.jpg","autograph":"","constellation":"0","birthday":"0000-00-00","home_img":"http://www.xiangyingchun.cn/upload/index/1.jpg"},{"id":"9","username":"13133133317","nickname":"初浦","sex":"1","hangye":"天文学家","age":"22","user_img":"http://www.xiangyingchun.cn/upload/20200603/97029165109c96948d1c7edf1a7d55cd.jpg","autograph":"","constellation":"0","birthday":"0000-00-00","home_img":"http://www.xiangyingchun.cn/upload/index/1.jpg"},{"id":"10","username":"13133133318","nickname":"表静丹","sex":"1","hangye":"电脑程式设计人员","age":"23","user_img":"http://www.xiangyingchun.cn/upload/20200603/97029165109c96948d1c7edf1a7d55cd.jpg","autograph":"","constellation":"0","birthday":"0000-00-00","home_img":"http://www.xiangyingchun.cn/upload/index/1.jpg"},{"id":"11","username":"13133133319","nickname":"寒星河","sex":"1","hangye":"系统分析师","age":"24","user_img":"http://www.xiangyingchun.cn/upload/20200603/97029165109c96948d1c7edf1a7d55cd.jpg","autograph":"","constellation":"0","birthday":"0000-00-00","home_img":"http://www.xiangyingchun.cn/upload/index/1.jpg"},{"id":"12","username":"13133133320","nickname":"段文乐","sex":"1","hangye":"道景师","age":"25","user_img":"http://www.xiangyingchun.cn/upload/20200603/97029165109c96948d1c7edf1a7d55cd.jpg","autograph":"","constellation":"0","birthday":"0000-00-00","home_img":"http://www.xiangyingchun.cn/upload/index/1.jpg"}]
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
         * id : 1
         * username : 13133133313
         * nickname :
         * sex : 0
         * hangye :
         * age : 0
         * user_img : http://www.xiangyingchun.cn/upload/20200603/97029165109c96948d1c7edf1a7d55cd.jpg
         * autograph : 个人签名设置
         * constellation : 0
         * birthday : 0000-00-00
         * home_img : http://www.xiangyingchun.cn/upload/index/1.jpg
         */

        private String id;
        private String username;
        private String nickname;
        private String sex;
        private String hangye;
        private String age;
        private String user_img;
        private String autograph;
        private String constellation;
        private String birthday;
        private String home_img;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getHangye() {
            return hangye;
        }

        public void setHangye(String hangye) {
            this.hangye = hangye;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getUser_img() {
            return user_img;
        }

        public void setUser_img(String user_img) {
            this.user_img = user_img;
        }

        public String getAutograph() {
            return autograph;
        }

        public void setAutograph(String autograph) {
            this.autograph = autograph;
        }

        public String getConstellation() {
            return constellation;
        }

        public void setConstellation(String constellation) {
            this.constellation = constellation;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getHome_img() {
            return home_img;
        }

        public void setHome_img(String home_img) {
            this.home_img = home_img;
        }
    }
}

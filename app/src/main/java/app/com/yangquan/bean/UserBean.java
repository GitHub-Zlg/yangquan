package app.com.yangquan.bean;

import java.util.List;

public class UserBean {
    /**
     * code : 0
     * msg : 获取信息成功
     * data : {"id":"10018","sex":"1","age":"24","label":["[自由职业"," 留学生"," 旅游从业者"," 会计"," 美容师"," 甜品师"," 保险"," 销售"," 飞行员"," 模特"," 搬砖"," 律师"," 发传单"," 工程师"," 上班族]"],"phone":"13512511029","hangye":"程序员","height":"172","region":"山西省-阳泉市-城区","wechat":"","reg_date":"2020-06-16","nickname":"好听的名字","user_img":"http://www.xiangyingchun.cn/upload/20200616/3fd46bdb07a0ec475f39441bb1001e18.jpg","birthday":"1996-03-22","autograph":"招聘ios开发111","constellation":"白羊座","wechat_show":"0"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 10018
         * sex : 1
         * age : 24
         * label : ["[自由职业"," 留学生"," 旅游从业者"," 会计"," 美容师"," 甜品师"," 保险"," 销售"," 飞行员"," 模特"," 搬砖"," 律师"," 发传单"," 工程师"," 上班族]"]
         * phone : 13512511029
         * hangye : 程序员
         * height : 172
         * region : 山西省-阳泉市-城区
         * wechat :
         * reg_date : 2020-06-16
         * nickname : 好听的名字
         * user_img : http://www.xiangyingchun.cn/upload/20200616/3fd46bdb07a0ec475f39441bb1001e18.jpg
         * birthday : 1996-03-22
         * autograph : 招聘ios开发111
         * constellation : 白羊座
         * wechat_show : 0
         */

        private String id;
        private String sex;
        private String age;
        private String phone;
        private String hangye;
        private String height;
        private String region;
        private String wechat;
        private String reg_date;
        private String nickname;
        private String user_img;
        private String birthday;
        private String autograph;
        private String constellation;
        private String wechat_show;
        private List<String> label;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getHangye() {
            return hangye;
        }

        public void setHangye(String hangye) {
            this.hangye = hangye;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }

        public String getReg_date() {
            return reg_date;
        }

        public void setReg_date(String reg_date) {
            this.reg_date = reg_date;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUser_img() {
            return user_img;
        }

        public void setUser_img(String user_img) {
            this.user_img = user_img;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
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

        public String getWechat_show() {
            return wechat_show;
        }

        public void setWechat_show(String wechat_show) {
            this.wechat_show = wechat_show;
        }

        public List<String> getLabel() {
            return label;
        }

        public void setLabel(List<String> label) {
            this.label = label;
        }
    }
}

package app.com.yangquan.bean;

public class UserBean {

    /**
     * code : 0
     * msg : 登录成功
     * data : {"id":"0000000003","sex":"2","age":"24","phone":"13512511028","hangye":"程序员","reg_date":"2020-06-03","nickname":"赵连岗","user_img":"/data/www/Xyc/public/upload/20200603/97029165109c96948d1c7edf1a7d55cd.jpg"}
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
         * id : 0000000003
         * sex : 2
         * age : 24
         * phone : 13512511028
         * hangye : 程序员
         * reg_date : 2020-06-03
         * nickname : 赵连岗
         * user_img : /data/www/Xyc/public/upload/20200603/97029165109c96948d1c7edf1a7d55cd.jpg
         */

        private String id;
        private String sex;
        private String age;
        private String phone;
        private String hangye;
        private String reg_date;
        private String nickname;
        private String user_img;

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
    }
}

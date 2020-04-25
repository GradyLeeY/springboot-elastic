package cn.imooc.demo.springboot.es.util;

public class Const {

    public enum TYPE{
        MYSQL("mysql"),
        ELASTICSEARCH("es");
        private String type;
        TYPE(String type){
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}

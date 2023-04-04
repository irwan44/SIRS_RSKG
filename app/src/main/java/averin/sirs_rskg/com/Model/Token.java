package averin.sirs_rskg.com.Model;

    //this is very simple class and it only contains the user attributes, a constructor and the getters
// you can easily do this by right click -> generate -> constructor and getters
    public class Token {

//        private int id;
        private String KodeApi, KeyApi, KeyCode, token;

        public Token(String token) {
//            this.KodeApi = KodeApi;
//            this.KeyApi = KeyApi;
//            this.KeyCode = KeyCode;
            this.token = token;
        }

//        public int getId() {
//            return id;
//        }

//        public String getKodeApi() {
//            return KodeApi;
//        }
//
//        public String getKeyApi() {
//            return KeyApi;
//        }
//
//        public String getKeyCode() {
//            return KeyCode;
//        }

        public String gettoken() { return token; }
}

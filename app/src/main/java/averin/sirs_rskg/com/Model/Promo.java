package averin.sirs_rskg.com.Model;

public class Promo {

        private String name;
        private String type;
        private int image;

        public Promo(String name, String type, int image) {
            this.name = name;
            this.type = type;
            this.image=image;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public int getImage() {
            return image;
        }
}

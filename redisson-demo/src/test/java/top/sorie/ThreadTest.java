package top.sorie;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreadTest {
    public enum RedisCacheEums {
        //节目与服务包对应关系
        ZX_PG_PK_,
        //节目跟产品对应关系
        ZX_PG_PD_,
        //产品跟服务关系
        ZX_PD_PK_,
        //节目与服务包对应关系
        HW_PG_PK_,
        //节目跟产品对应关系
        HW_PG_PD_,
        //产品跟服务关系
        HW_PD_PK_;
        private String cacheCode;

        public String getCacheCode() {
            return cacheCode;
        }

        public void setCacheCode(String cacheCode) {
            this.cacheCode = cacheCode;
        }
    }
    @Test
    public void newTread() throws ParseException {
        System.out.println(new Date().getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(format.parse("2021-08-24 07:52:00").getTime());
        System.out.println(format.parse("2021-08-24 07:56:00").getTime());
    }
}

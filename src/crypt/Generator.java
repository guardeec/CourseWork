package crypt;

import POJO.Params;

import java.math.BigInteger;
import java.util.Map;

/**
 * Created by anna on 12/10/15.
 */
public class Generator {

    public static void main(String[] args) {
        Params params = new Params();
        params.initStartParams();
        params.initXandY();
        params.initHash();
        params.initSign();
        System.out.println(params.checkSign());
    }
}

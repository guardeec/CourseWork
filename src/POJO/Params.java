package POJO;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by anna on 12/10/15.
 */
public class Params {
    private int stage = 0;

    private BigInteger gamma;
    private BigInteger p;
    private BigInteger a;

    private BigInteger x;
    private BigInteger y;

    private BigInteger u;
    private BigInteger z;

    private BigInteger g;
    private BigInteger k;
    private BigInteger s;

    private BigInteger left;
    private BigInteger right;

    /*
    генерируем p, a и y`
     */
    public void initStartParams(){
        //чистим зависимые значения
        clearStagesAfterStartParams();
        //процедура генерации, подбираем такие значения что бы выполнялось (a^y` != 1 mod p)
        do {
            BigInteger p0 = BigInteger.probablePrime(96, new SecureRandom()).subtract(BigInteger.ONE);
            do {
                this.gamma = BigInteger.probablePrime(160, new SecureRandom());
                this.p = gamma.multiply(p0).add(BigInteger.ONE);
            }while (!this.p.isProbablePrime(5000));
            this.a = BigInteger.probablePrime(128, new SecureRandom()).modPow(p0, p);
        }while (this.a.modPow(this.gamma,this.p).compareTo(BigInteger.ONE)!=0);
    }
    /*
    заносим ручками p, a и y`
     */
    public void initStartParams(BigInteger p, BigInteger a, BigInteger gamma){
        //чистим зависимые значения
        clearStagesAfterStartParams();
        //проверяем значения на корректность (a^y` != 1 mod p) и p - простое
        if (p.isProbablePrime(5000) && a.modPow(gamma,p).compareTo(BigInteger.ONE)==0 && gamma.bitLength()>128){
            this.p = p;
            this.gamma = gamma;
            this.a = a;
        }else {
            //если не корректно то кидаем исключение
            this.p = null;
            this.gamma = null;
            this.a = null;
            throw new ArithmeticException("Неверные параметры p, a и gamma");
        }
    }

    /*
    генерируем ключи x и y
     */
    public void initXandY(){
        //чистим зависимые значения
        clearStagesAfterXandY();
        this.x = new BigInteger(this.p.subtract(BigInteger.ONE).bitLength(), new SecureRandom());
        this.y = this.a.modPow(this.x, this.p);
    }
    /*
    заносим ручками x и y
     */
    public void initXandY(BigInteger x){
        //чистим зависимые значения
        clearStagesAfterXandY();
        //проверяем значения на корректность (длинна)
        if (x.bitLength()>3 && x.bitLength()<this.p.subtract(BigInteger.ONE).bitLength()){
            this.y = this.a.modPow(this.x, this.p);
        }else {
            //если не корректно то кидаем исключение
            this.x = null;
            this.y = null;
            throw new ArithmeticException("Неправильная длинна x");
        }
    }

    /*
    генерируем хэш
     */
    public void initHash(){
        //чистим зависимые значения
        clearStagesAfterHash();
        //заносим хэш/сообщение
        this.u = new BigInteger(this.p.subtract(BigInteger.ONE).bitLength(), new SecureRandom());
        //z = a^u mod p
        this.z = this.a.modPow(this.u,this.p);
        //генерируем подпись (больше значений вводить не нужно, поэтому можно сразу)
        initSign();
    }
    /*
    заносим ручками хэш
     */
    public void initHash(BigInteger u){
        //чистим зависимые значения
        clearStagesAfterHash();
        //проверяем значения на корректность (длинна)
        if (u.bitLength()>3){
            this.u = u;
            this.z = this.a.modPow(this.u,this.p);
            initSign();
        }else {
            //если не корректно то кидаем исключение
            this.u = null;
            this.z = null;
            throw new ArithmeticException("Неправильная длинна u");
        }
    }

    /*
    генерируем подпись (вызывается сразу после генерации хэша)
     */
    public void initSign(){
        // g = ((u*x) / (z-u)) mod(y`)
        this.g = this.u.multiply(this.x).multiply(this.z.subtract(this.u).modInverse(this.gamma)).mod(this.gamma);
        // k = ((z-u) / x) mod (y`)
        this.k = this.z.subtract(this.u).multiply(this.x.modInverse(this.gamma)).mod(this.gamma);
        // s = a ^ g mod (p)
        this.s = this.a.modPow(this.g, this.p);
        // right = a ^ ( S ^ k mod (p)) mod (p)
        this.right = this.a.modPow(this.s.modPow(this.k,this.p),this.p);
    }

    /*
    проверяем подпись - обратить внимание что не только заполняет левуя часть уравнения но и возвращает результат проверки
     */
    public boolean checkSign(){
        //left = (Sy)^k
        this.left = this.s.multiply(this.y).modPow(this.k, this.p);
        // left == right ?
        return left.compareTo(right)==0;
    }

    /*
    -------------------------------------------------------------------------
    ниже описаны функции необходимые для функционирования GUI
    -------------------------------------------------------------------------
     */

    /*
    Чистит параметры если мы возвращаемся
    например:   если проинициализированы начальные параметры и параметры x и y
                а потом снова инициализируются начальные параметры
                то x и y должны быть очищенны
     */
    private void clearStagesAfterStartParams(){
        this.x = null;
        this.y = null;
        this.u = null;
        this.z = null;
        this.g = null;
        this.k = null;
        this.s = null;
        this.left = null;
        this.right = null;
    }
    private void clearStagesAfterXandY(){
        this.u = null;
        this.z = null;
        this.g = null;
        this.k = null;
        this.s = null;
        this.left = null;
        this.right = null;
    }
    private void clearStagesAfterHash(){
        this.g = null;
        this.k = null;
        this.s = null;
        this.left = null;
        this.right = null;
    }


    /*
        Гэттеры что бы заполнять поля в гуи
    */
    public BigInteger getGamma() {
        return gamma;
    }
    public BigInteger getP() {
        return p;
    }
    public BigInteger getA() {
        return a;
    }
    public BigInteger getX() {
        return x;
    }
    public BigInteger getY() {
        return y;
    }
    public BigInteger getU() {
        return u;
    }
    public BigInteger getZ() {
        return z;
    }
    public BigInteger getG() {
        return g;
    }
    public BigInteger getK() {
        return k;
    }
    public BigInteger getS() {
        return s;
    }
    public BigInteger getLeft() {
        return left;
    }
    public BigInteger getRight() {
        return right;
    }
}

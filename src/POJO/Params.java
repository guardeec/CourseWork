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
    генерируем или заносим начальные параметры
     */
    public void initStartParams(){
        clearStagesAfterStartParams();
        do {
            BigInteger p0 = BigInteger.probablePrime(32, new SecureRandom()).subtract(BigInteger.ONE);
            do {
                this.gamma = BigInteger.probablePrime(16, new SecureRandom());
                this.p = gamma.multiply(p0).add(BigInteger.ONE);
            }while (!this.p.isProbablePrime(5000));
            this.a = BigInteger.probablePrime(32, new SecureRandom()).modPow(p0, p);
        }while (this.a.modPow(this.gamma,this.p).compareTo(BigInteger.ONE)!=0);
    }
    public void initStartParams(BigInteger p, BigInteger a, BigInteger gamma){
        clearStagesAfterStartParams();
        if (p.isProbablePrime(5000) && a.modPow(gamma,p).compareTo(BigInteger.ONE)==0){
            this.p = p;
            this.gamma = gamma;
            this.a = a;
        }else {
            throw new ArithmeticException("Неверные параметры p, a и gamma");
        }
    }

    /*
    генерируем ключи x и y
     */
    public void initXandY(){
        clearStagesAfterXandY();
        this.x = new BigInteger(32, new SecureRandom());
        this.y = this.a.modPow(this.x, this.p);
    }
    public void initXandY(BigInteger x){
        clearStagesAfterXandY();
        if (x.bitLength()>3 && x.bitLength()<32){
            this.y = this.a.modPow(this.x, this.p);
        }else {
            throw new ArithmeticException("Неправильная длинна x");
        }
    }

    /*
    генерируем хэш
     */
    public void initHash(){
        clearStagesAfterHash();
        this.u = new BigInteger(15, new SecureRandom());
        this.z = this.a.modPow(this.u,this.p);
        initSign();
    }
    public void initHash(BigInteger u){
        clearStagesAfterHash();
        if (u.bitLength()>3 && u.bitLength()<this.p.subtract(BigInteger.ONE).bitLength()){
            this.u = u;
            this.z = this.a.modPow(this.u,this.p);
            initSign();
        }else {
            throw new ArithmeticException("Неправильная длинна u");
        }
    }

    /*
    генерируем подпись (вызывается сразу после генерации хэша)
     */
    public void initSign(){
        this.g = this.u.multiply(this.x).multiply(this.z.subtract(this.u).modInverse(this.gamma)).mod(this.gamma);
        this.k = this.z.subtract(this.u).multiply(this.x.modInverse(this.gamma)).mod(this.gamma);
        this.s = this.a.modPow(this.g, this.p);
        this.right = this.a.modPow(this.s.modPow(this.k,this.p),this.p);
    }

    /*
    проверяем подпись - обратить внимание что не только заполняет левуя часть уравнения но и возвращает результат проверки
     */
    public boolean checkSign(){
        this.left = this.s.multiply(this.y).modPow(this.k, this.p);
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

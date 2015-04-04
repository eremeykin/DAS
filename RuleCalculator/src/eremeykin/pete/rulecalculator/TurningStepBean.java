/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.rulecalculator;

/**
 *
 * @author Pete
 */
public class TurningStepBean {

    /**
     * @return the diameter
     */
    public double getDiameter() {
        return diameter;
    }

    /**
     * @param diameter the diameter to set
     */
    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    /**
     * @return the type
     */
    public TurningType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(TurningType type) {
        this.type = type;
    }

    /**
     * @return the kind
     */
    public ProcessKind getKind() {
        return kind;
    }

    /**
     * @param kind the kind to set
     */
    public void setKind(ProcessKind kind) {
        this.kind = kind;
    }

    /**
     * @return the workpieceMaterial
     */
    public WorkpieceMaterial getWorkpieceMaterial() {
        return workpieceMaterial;
    }

    /**
     * @param workpieceMaterial the workpieceMaterial to set
     */
    public void setWorkpieceMaterial(WorkpieceMaterial workpieceMaterial) {
        this.workpieceMaterial = workpieceMaterial;
    }

    /**
     * @return the workpieceType
     */
    public WorkpieceType getWorkpieceType() {
        return workpieceType;
    }

    /**
     * @param workpieceType the workpieceType to set
     */
    public void setWorkpieceType(WorkpieceType workpieceType) {
        this.workpieceType = workpieceType;
    }

    /**
     * @return the instrumentMaterial
     */
    public InstrumentMaterial getInstrumentMaterial() {
        return instrumentMaterial;
    }

    /**
     * @param instrumentMaterial the instrumentMaterial to set
     */
    public void setInstrumentMaterial(InstrumentMaterial instrumentMaterial) {
        this.instrumentMaterial = instrumentMaterial;
    }

    /**
     * @return the roughness
     */
    public double getRoughness() {
        return roughness;
    }

    /**
     * @param ra the roughness to set
     */
    public void setRoughness(double ra) {
        this.roughness = ra;
    }

    /**
     * @return the instrumentRadius
     */
    public double getInstrumentRadius() {
        return instrumentRadius;
    }

    /**
     * @param instrumentRadius the instrumentRadius to set
     */
    public void setInstrumentRadius(double instrumentRadius) {
        this.instrumentRadius = instrumentRadius;
    }

    /**
     * @return the v
     */
    public double getV() {
        return v;
    }

    /**
     * @param v the v to set
     */
    public void setV(double v) {
        System.out.println("SET V " + v);
        this.v = v;
    }

    /**
     * @return the p
     */
    public double getP() {
        return p;
    }

    /**
     * @param p the p to set
     */
    public void setP(double p) {
        System.out.println("SET P " + p);
        this.p = p;
    }

    /**
     * @return the n
     */
    public double getN() {
        return n;
    }

    /**
     * @param n the n to set
     */
    public void setN(double n) {
        System.out.println("SET N " + n);
        this.n = n;
    }

    /**
     * @return the f
     */
    public double getF() {
        return f;
    }

    /**
     * @param f the f to set
     */
    public void setF(double f) {
        System.out.println("SET F " + f);
        this.f = f;
    }

    /**
     * @return the m
     */
    public double getM() {
        return m;
    }

    /**
     * @param m the m to set
     */
    public void setM(double m) {
        System.out.println("SET M " + m);
        this.m = m;
    }

    /**
     * @return the t
     */
    public double getT() {
        return t;
    }

    /**
     * @param t the t to set
     */
    public void setT(double t) {
        System.out.println("set t " + t);

        this.t = t;
    }

    /**
     * @return the s
     */
    public double getS() {
        System.out.println("get s " + s);
        return s;
    }

    /**
     * @param s the s to set
     */
    public void setS(double s) {
        System.out.println("set s " + s);
        this.s = s;
    }

    /**
     * @return the kmv
     */
    public double getKmv() {
        return kmv;
    }

    /**
     * @param kmv the kmv to set
     */
    public void setKmv(double kmv) {
        System.out.println("set kmv " + kmv);
        this.kmv = kmv;
    }

    /**
     * @return the kpv
     */
    public double getKpv() {
        return kpv;
    }

    /**
     * @param kpv the kpv to set
     */
    public void setKpv(double kpv) {
        System.out.println("set kpv " + kpv);
        this.kpv = kpv;
    }

    /**
     * @return the kiv
     */
    public double getKiv() {
        System.out.println("get kiv " + kiv);
        return kiv;
    }

    /**
     * @param kiv the kiv to set
     */
    public void setKiv(double kiv) {
        System.out.println("set kiv " + kiv);
        this.kiv = kiv;
    }

    /**
     * @return the cv
     */
    public double getCv() {
        return cv;
    }

    /**
     * @param cv the cv to set
     */
    public void setCv(double cv) {
        System.out.println("set cv " + cv);
        this.cv = cv;
    }

    /**
     * @return the xv
     */
    public double getXv() {
        return xv;
    }

    /**
     * @param xv the xv to set
     */
    public void setXv(double xv) {
        System.out.println("set xv " + xv);
        this.xv = xv;
    }

    /**
     * @return the yv
     */
    public double getYv() {
        return yv;
    }

    /**
     * @param yv the yv to set
     */
    public void setYv(double yv) {
        System.out.println("set yv " + yv);
        this.yv = yv;
    }

    /**
     * @return the mv
     */
    public double getMv() {
        return mv;
    }

    /**
     * @param mv the mv to set
     */
    public void setMv(double mv) {
        System.out.println("set mv " + mv);
        this.mv = mv;
    }

    /**
     * @return the cp
     */
    public double getCp() {
        return cp;
    }

    /**
     * @param cp the cp to set
     */
    public void setCp(double cp) {
        System.out.println("set cp " + cp);
        this.cp = cp;
    }

    /**
     * @return the xp
     */
    public double getXp() {
        return xp;
    }

    /**
     * @param xp the xp to set
     */
    public void setXp(double xp) {
        System.out.println("set xp " + xp);
        this.xp = xp;
    }

    /**
     * @return the yp
     */
    public double getYp() {
        return yp;
    }

    /**
     * @param yp the yp to set
     */
    public void setYp(double yp) {
        System.out.println("set yp " + yp);
        this.yp = yp;
    }

    /**
     * @return the np
     */
    public double getNp() {
        return np;
    }

    /**
     * @param np the np to set
     */
    public void setNp(double np) {
        System.out.println("set np " + np);
        this.np = np;
    }

    /**
     * @return the kmp
     */
    public double getKmp() {
        return kmp;
    }

    /**
     * @param kmp the kmp to set
     */
    public void setKmp(double kmp) {
        System.out.println("set kmp " + kmp);
        this.kmp = kmp;
    }

    /**
     * @return the incorrect
     */
    public boolean isIncorrect() {
        return incorrect;
    }

    /**
     * @param incorrect the incorrect to set
     */
    public void setIncorrect(boolean incorrect) {
        this.incorrect = incorrect;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) throws StepIncorrect {
        incorrect = true;
        this.errorMessage = errorMessage;
        throw new StepIncorrect();
    }

    /**
     * @return the durability
     */
    public double getDurability() {
        return durability;
    }

    /**
     * @param durability the durability to set
     */
    public void setDurability(double durability) {
        this.durability = durability;
    }

    public static enum TurningType {

        ROUGH("Черновое"),
        SEMI_ROUGH("Получистовое"),
        FINISH("Чистовое");
        private String name;

        private TurningType(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }

    }

    public static enum ProcessKind {

        EXTERNAL_TURNING("Внешнее точение"),
        BORING("Растачивание"),
        GROOVING("Подрезание"),
        PARTING("Отрезание");
//        TAPPING("Нарезание резьбы");

        private String name;

        private ProcessKind(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }

    }

    public static enum WorkpieceMaterial {

        STEEL("Сталь"),
        CAST_IRON("Чугун");
        private String name;

        private WorkpieceMaterial(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    public static enum WorkpieceType {

        FORGING("Поковка"),
        CASTING("Отливка"),
        ROLLING("Прокат"),
        CRUSTLESS("Полуфабрикат без корки");
        private String name;

        private WorkpieceType(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    public static enum InstrumentMaterial {

        T15K10("Т15К10"),
        T15K6("Т15Л6"),
        T30K4("Т30К4"),
        BK8("ВК8"),
        BK6("ВК6"),
        BK4("ВК4"),
        HIGH_SPEED_STEEL("Быстрорежущая сталь");

        private String name;

        private InstrumentMaterial(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    // входные параметры
    private double diameter;// диаметр заготовки
    private TurningType type = TurningType.ROUGH;// тип точения
    private ProcessKind kind = ProcessKind.EXTERNAL_TURNING;// вид обработки
    private WorkpieceMaterial workpieceMaterial = WorkpieceMaterial.STEEL;// материал заготовки
    private WorkpieceType workpieceType = WorkpieceType.FORGING;// тип заготовки
    private InstrumentMaterial instrumentMaterial = InstrumentMaterial.T15K10;// материал инструмента
    private double durability = 20d;//стойкость резца
    private double roughness;// шероховатость
    private double instrumentRadius;//радиус резца при вершине
    // внутренние параметры
    private double t;//глубина резания
    private double s;//подача
    private double kmv;//поправка на материал детали
    private double kpv;//поправка на состояние поверхности
    private double kiv;//поправка на материал инструмента
    private double cv;//эмпирический коэффициент скорости резания
    private double xv;//эмпирический коэффициент скорости резания
    private double yv;//эмпирический коэффициент скорости резания
    private double mv;//эмпирический коэффициент скорости резания
    private double cp;//эмпирический коэффициент силы резания
    private double xp;//эмпирический коэффициент силы резания
    private double yp;//эмпирический коэффициент силы резания
    private double np;//эмпирический коэффициент силы резания
    private double kmp;//поправка на обрабатываемый материал
    //выходные параметры
    private double v;//скорость резания
    private double p;//сила резания
    private double n;//мощность резания
    private double f;//частота вращения
    private double m;//момент резания
    //
    private boolean incorrect = false; //Показывает есть ли ошибка
    private String errorMessage = "Ошибок нет";

//    @Override
    public String toStr() {
        String result = "\nd = " + diameter + "\n"
                + "type = " + type + "\n"
                + "kind = " + kind + "\n"
                + "workpieceMaterial = " + workpieceMaterial + "\n"
                + "workpieceType = " + workpieceType + "\n"
                + "instrumentMaterial = " + instrumentMaterial + "\n"
                + "roughness = " + roughness + "\n"
                + "instrumentRadius = " + instrumentRadius + "\n"
                + "t = " + t + "\n"
                + "s = " + s + "\n"
                + "kmv = " + kmv + "\n"
                + "kpv = " + kpv + "\n"
                + "kiv = " + kiv + "\n"
                + "cv = " + cv + "\n"
                + "xv = " + xv + "\n"
                + "yv = " + yv + "\n"
                + "mv = " + mv + "\n"
                + "cp = " + cp + "\n"
                + "xp = " + xp + "\n"
                + "yp = " + yp + "\n"
                + "np = " + np + "\n"
                + "kmp = " + kmp + "\n"
                + "incorrect = " + incorrect + "\n"
                + "errorMessage = " + errorMessage;
        return result;
    }

    public static class StepIncorrect extends Exception {

    }
}

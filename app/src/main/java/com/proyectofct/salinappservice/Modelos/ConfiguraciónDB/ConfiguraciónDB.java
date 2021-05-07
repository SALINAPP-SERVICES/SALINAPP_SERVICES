package com.proyectofct.salinappservice.Modelos.ConfiguraciónDB;

public class ConfiguraciónDB {
    public static final String NOMBREDB = "proyectoempresa";
    public static final String HOSTDB = "infsalinas.sytes.net";
    public static final String USUARIODB = "usuario";
    public static final String CLAVEDB = "dam1234";
    public static final String PUERTOMYSQL = "5306";
    private static final String OPCIONES = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    public static final String URLMYSQL = "jdbc:mysql://"+ HOSTDB + ":" + PUERTOMYSQL+"/" + NOMBREDB + OPCIONES;
}

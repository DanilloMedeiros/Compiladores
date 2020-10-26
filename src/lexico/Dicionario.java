package lexico;

public class Dicionario {

	public static String tipos = "integer|real|boolean";

	public final static String palavraReservada = "program|var|" + tipos + "|procedure|begin"
			+ "|end|if|then|else|while|for|do|not|to" + "|case|true|false";
	public final static String palavra = "[a-z]*|_*|[A-Z]*";
	public final static String identificador = "\\_*\\w+[\\_\\w+]*";
	public final static String delimitadores = "\\;|\\.|\\:|\\,|\\(|\\)";
	public final static String operadoresRelacionais = "=|<|>|<=|>=|<>";
	public final static String operadoresAditivos = "\\+|\\-";
	public final static String operadorAditivoOr = "or";
	public final static String operadorMultiplicativo = "\\*|/";
	public final static String operadorMultiplicativoAnd = "and";
	public final static String atribuicao = "\\:=";
	public final static String numerosInteiros = "\\d+";
	public final static String numerosReais = "\\d+\\.{1}\\d*";
	public final static String comentario = ".*\\w+\\}{1}";
	public final static String potencia = "\\d+\\.{1}\\d*\\e[+|-]\\d*";
	public final static String espacoTabulacao = " |\t";
	public final static String espaco = "\s";

	// TOKEN
	public static String PROGRAM = "program";
	public static String VAR = "var";

	// CLASSE
	public static String PALAVRA_RESERVADA = "PALAVRA_RESERVADA";
	public static String DELIMITADOR = "DELIMITADOR";
	public static String IDENTIFICADOR = "IDENTIFICADOR";
	public static String NUMERO_INTEIRO = "NUMERO_INTEIRO";
	public static String NUMERO_REAL = "NUMERO_REAL";
	public static String OPERADOR_MULTIPLICATIVO = "OPERADOR_MULTIPLICATIVO";
	public static String ATRIBUICAO = "ATRIBUICAO";

}

package lexico;

public class Dicionario {

    public String palavraReservada = "program|var|" + "integer|real|boolean|procedure|begin"
			+ "|end|if|then|else|while|do|not" + "|case|true|false";
	public String palavra = "[a-z]*|_*|[A-Z]*";
	public String identificador = "\\_*\\w+[\\_\\w+]*";
	public String delimitadores = "\\;|\\.|\\:|\\,|\\(|\\)";
	public String operadoresRelacionais = "=|<|>|<=|>=|<>";
	public String operadoresAditivos = "\\+|\\-";
	public String operadorAditivoOr = "or";
	public String operadorMultiplicativo = "\\*|/";
	public String operadorMultiplicativoAnd = "and";
	public String atribuicao = "\\:=";
	public String numerosInteiros = "\\d+";
	public String numerosReais = "\\d+\\.{1}\\d*";
	public String comentario = ".*\\w+\\}{1}";
	public String potencia = "\\d+\\.{1}\\d*\\e[+|-]\\d*";
	public String espacoTabulacao = " |\t";
	public String espaco = "\s";
    
}

package sintatico;

import java.util.List;
import lexico.*;

public class Sintatico {

    private List<Simbolo> tabela;
    Simbolo simbolo;
    private int index;

    public Sintatico(List<Simbolo> tabela) {
        this.tabela = tabela;
        this.index = 0;
        this.simbolo = tabela.get(index);
    }

    public void analisar() {
        // for (Simbolo simbolo : tabela) {
        // System.out.println(simbolo.toString());
        // }

        programa();

    }

    private void next() {

        if (index < tabela.size() - 1) {
            this.index++;
            this.simbolo = tabela.get(index);
        } else {
            System.out.println("fim do programa");
            System.exit(0);
        }
    }

    private void back() {

        if (index >= 0) {
            this.index--;
            this.simbolo = tabela.get(index);
        }
    }

    private void Error(String msg) {
        System.err.println("Error " + msg);
        System.err.println("line " + simbolo.linha);
        System.exit(0);
    }

    private boolean programa() {

        if (!program_id()) {
            return false;
        }

        next();
        declaracoes_variaveis();
        next();
        System.out.println(declaracoes_de_subprogramas());
        next();
        System.out.println(comando_composto());

        // if (!simbolo.token.equals("."))
        // Error();
        return true;
    }

    private boolean program_id() {

        boolean is_program_id = tabela.get(0).token.equals(Dicionario.PROGRAM)
                && tabela.get(1).classificacao.equals(Dicionario.IDENTIFICADOR)
                && tabela.get(2).classificacao.equals(Dicionario.DELIMITADOR);

        // ja foram verificadas 3 linhas da tabela
        if (is_program_id) {
            next();
            next();
        }

        return is_program_id;
    }

    private boolean declaracoes_variaveis() {

        if (!simbolo.token.equals(Dicionario.VAR))
            return false;

        next();
        return lista_de_identificadores();
    }

    private boolean lista_de_identificadores() {

        if (simbolo.classificacao.equals(Dicionario.IDENTIFICADOR)) {
            next();
            return lista_de_delimitadores();
        }

        back();
        return false;

    }

    private boolean lista_de_delimitadores() {

        switch (simbolo.token) {
            case ",":

                next();
                return lista_de_identificadores();

            case ":":

                next();

                if (!tipo_isValid())
                    break;

                next();
                return lista_de_delimitadores();

            case ";":
                return true;

        }
        return false;
    }

    private boolean tipo_isValid() {
        return simbolo.token.matches(Dicionario.tipos);
    }

    private boolean declaracoes_de_subprogramas() {
        if (declaracao_de_subprograma()) {
            return true;
        }
        return false;
    }

    private boolean declaracao_de_subprograma() {
        if (simbolo.token.equals("procedure")) {
            next();

            if (simbolo.classificacao.equals(Dicionario.IDENTIFICADOR)) {
                next();

                if (argumentos()) {
                    next();

                    if (simbolo.token.equals(";")) {
                        next();

                        if (declaracoes_variaveis()) {
                            next();
                            declaracoes_de_subprogramas();
                            return comando_composto();
                        }
                    } else {
                        Error("Falta o token ;");
                    }
                }
            }
        }
        return false;
    }

    private boolean argumentos() {
        if (simbolo.token.equals("(")) {
            next();

            if (lista_de_parametros()) {
                next();

                if (simbolo.token.equals(")"))
                    return true;
                else
                    Error("Falta o token )");

            }

        } else
            Error("Falta o token (");
        return false;
    }

    private boolean lista_de_parametros() {

        if (simbolo.classificacao.equals(Dicionario.IDENTIFICADOR)) {
            next();

            if (simbolo.token.equals(":")) {
                next();

                if (tipo_isValid()) {
                    next();

                    if (simbolo.token.equals(",")) {
                        next();
                        lista_de_parametros();
                        return true;
                    }
                }

            } else {
                Error("Falta o delimitador :");
                return false;
            }

        } else {
            Error("Falta identificação da variável");
            return false;
        }
        back();

        return true;
    }

    private boolean comando_composto() {
        if (simbolo.token.equals("begin")) {
            next();

            if (comandos_opcionais()) {
                next();

                if (simbolo.token.equals("end"))
                    return true;
                else
                    Error("Falta o token end");

            }

        } else
            Error("Falta o token end");

        return false;
    }

    private boolean comandos_opcionais() {
        if (lista_de_comandos()) {
            return true;
        }
        return false;
    }

    private boolean lista_de_comandos() {

        if (comando()) {
            next();

            if (lista_de_comandos()) {
                // next();

                return true;

            } else {
                back();
                return true;
            }

        }

        return false;
    }

    private boolean comando() {
        if (variavel()) {
            next();

            if (simbolo.token.equals(":=")) {
                next();

                if (expressao()) {
                    next();

                    if (simbolo.token.equals(";"))
                        return true;
                }

            }

            return true;
        } else if (simbolo.token.equals("while")) {
            next();
        }

        return false;
    }

    private boolean variavel() {
        if (!simbolo.classificacao.equals(Dicionario.IDENTIFICADOR)) {
            // Error("Variável não reconhecida");
            return false;
        }
        return true;
    }

    private boolean lista_de_expressoes() {
        if (expressao()) {
            return true;
        }
        return false;
    }

    private boolean expressao() {
        if (expressao_simples()) {
            if (op_relacional()) {
                if (expressao()) {
                    return true;
                }
            }
            return true;
        }

        return false;
    }

    private boolean expressao_simples() {
        if (termo()) {
            next();
            if (op_aditivo()) {
                next();
                return expressao_simples();
            } else {
                back();
            }
            return true;
        }

        return false;
    }

    private boolean termo() {
        if (fator()) {
            next();

            if (op_multiplicativo()) {
                next();

                if (fator())
                    return true;
                else
                    Error("Falta identificacao da varivável");

            } else {
                back();
                return true;
            }
        }
        return false;
    }

    private boolean fator() {
        if (simbolo.classificacao.equals(Dicionario.IDENTIFICADOR)) {
            next();

            if (simbolo.token.equals("(")) {
                next();

                if (lista_de_expressoes()) {
                    next();

                    if (simbolo.token.equals(")"))
                        return true;

                }

            } else {
                back();
            }

            return true;

        } else {
            Error("Falta identificacao de variavel");
        }

        return false;
    }

    private boolean op_aditivo() {
        String aditivo = Dicionario.operadorAditivoOr + "|" + Dicionario.operadoresAditivos;
        return simbolo.token.matches(aditivo);
    }

    private boolean op_relacional() {
        return simbolo.token.matches(Dicionario.operadoresRelacionais);
    }

    private boolean op_multiplicativo() {
        String multiplicativo = Dicionario.operadorMultiplicativo + "|" + Dicionario.operadorMultiplicativoAnd;
        return simbolo.token.matches(multiplicativo);
    }

}

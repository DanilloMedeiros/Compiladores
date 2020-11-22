package sintatico;

import java.util.List;
import lexico.*;
import semantico.Identificador;
import semantico.Semantico;

public class Sintatico {

    private List<Simbolo> tabela;
    private Semantico semantico;
    private Identificador id;
    Simbolo simbolo;
    private int index;

    public Sintatico(List<Simbolo> tabela) {
        this.tabela = tabela;
        this.index = 0;
        this.simbolo = tabela.get(index);
        this.semantico = new Semantico();
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
        // System.exit(0);
    }

    private boolean programa() {

        if (!program_id()) {
            return false;
        }

        next();

        declaracoes_variaveis();
        next();
        declaracoes_de_subprogramas();
        comando_composto();// fim
        next();// pega o ponto.
        if (simbolo.token.equals(".")) {
            System.out.println("Fim do programa");
            System.exit(0);
        }

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
            id = new Identificador(tabela.get(1).classificacao, tabela.get(0).token);

            if(semantico.isEquals( id.nome, id.tipo)) {
                System.out.println("Erro Semântico, nome do programa é igual a " + tabela.get(1).classificacao);
                return false;
            }

            next();
            next();
        }

        return is_program_id;
    }

    private boolean declaracoes_variaveis() {

        if (simbolo.token.equals(Dicionario.VAR)) {
            next();
            return lista_declaracoes_variaveis();

        }

        return false;
    }

    private boolean lista_declaracoes_variaveis() {

        if (simbolo.classificacao.equals(Dicionario.IDENTIFICADOR)) {

            semantico.pushId(new Identificador(simbolo.token, Dicionario.STR_NULO));

            next();

            if (simbolo.token.equals(",")) {
                next();
                return lista_declaracoes_variaveis();

            } else if (simbolo.token.equals(":")) {
                next();

                if (tipo()) {

                    semantico.addTipoNasVariaveis(simbolo.token);

                    next();
                    return simbolo.token.equals(";");
                }

            }
        } else {
            Error("Variavel não identificada");
        }

        return false;
    }

    private boolean tipo() {
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
                            return true;
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

                if (tipo()) {
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
                if (simbolo.token.equals("end")) {
                    next();
                    if (simbolo.token.equals("end")) {
                        next();
                        simbolo.token.equals(".");
                        System.out.println("Programa concluido com sucesso");
                        System.exit(0);
                        // return true;
                    } else if (simbolo.token.equals("begin")) {
                        comando_composto();
                    }
                    return true;
                } else
                    Error("Falta o token end");
            } else {
                Error("Erro no comando opcional");
                return false;
            }
        }
        // Error("Erro sem begin");
        return false;
    }

    private boolean comandos_opcionais() {
        if (lista_de_comandos()) {
            return true;
        }
        return true;
    }

    private boolean lista_de_comandos() {

        if (comando()) {
            if (simbolo.token.equals(";")) {
                next();
                if (simbolo.token.equals("end"))
                    return true;
                else if (lista_de_comandos())
                    return true;

            }
        }
        Error("Erro na linha de comandos");
        return false;
    }

    private boolean comando() {

        if (variavel()) {
            if (ativacaoDeProcedimento()) {
                return true;
            } else {
                next();
                if (simbolo.token.equals(":=")) {
                    next();
                    return expressao();
                }
            }
        } else if (comando_composto()) {
            return true;

        } else if (simbolo.token.equals("if")) {
            next();
            if (expressao()) {
                if (simbolo.token.equals("then")) {
                    next();
                    if (comando()) {
                        next();
                        if (simbolo.token.equals("else")) {
                            next();
                            return comando();
                        } else {
                            next();
                            return true;
                        }
                    }
                }
            }

        } else if (simbolo.token.equals("while")) {
            next();
            if (expressao()) {
                if (simbolo.token.equals("do")) {
                    next();
                    return comando();
                } else {
                    Error("Erro no while");
                    return false;
                }
            }
        } else if (simbolo.token.equals("case")) {
            next();
            if (simbolo.classificacao.equals(Dicionario.NUMERO_INTEIRO)) {
                next();
                if (simbolo.token.equals("of")) {
                    next();
                    if (selecao()) {
                        if (simbolo.token.equals("else")) {
                            next();
                            if (lista_de_comandos()) {
                                next();
                                if (simbolo.token.equals("end")) {
                                    next();
                                    if (simbolo.token.equals(";")) {
                                        return true;
                                    } else {
                                        Error("erro no ;");
                                        return false;
                                    }
                                } else {

                                }
                            }
                        } else {
                            next();
                            return true;
                        }

                    } else {
                        Error("erro no case");
                        return false;
                    }
                } else {
                    Error("erro no case");
                    return false;
                }

            } else {
                Error("erro no case");
                return false;
            }
        } else {
            return false;
        }
        return false;
    }

    public boolean selecao() {
        if (simbolo.classificacao.equals(Dicionario.NUMERO_INTEIRO)) {
            next();
            if (simbolo.token.equals(":")) {
                next();
                if (comando()) {
                    if (simbolo.token.equals(";")) {
                        next();
                        // mudar para zerar o comando
                        if (simbolo.classificacao.equals(Dicionario.NUMERO_INTEIRO)) {
                            return selecao();
                        } else {
                            return true;
                        }
                    } else {
                        Error("erro no case");
                        return false;
                    }
                } else {
                    Error("erro no case");
                    return false;
                }
            } else {
                Error("erro no case");
                return false;
            }
        } else {
            Error("erro no case");
            return false;
        }
    }

    private boolean ativacaoDeProcedimento() {
        if (simbolo.classificacao.equals(Dicionario.IDENTIFICADOR)) {
            next();
            if (simbolo.token.equals("(")) {
                next();
                if (lista_de_expressoes()) {
                    if (simbolo.token.equals(")")) {
                        next();
                        return true;
                    } else {
                        Error("Erro na ativaçao");
                        return false;
                    }
                }

            } else {
                back();
                return false;
            }
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
                next();
                return expressao_simples();
            }
            return true;
        }

        return false;
    }

    private boolean expressao_simples() {
        if (termo()) {
            if (op_aditivo()) {
                next();
                return expressao_simples();
            }
            return true;

        } else if (sinal()) {
            return termo();
        }

        Error("Falta um termo");

        return false;
    }

    private boolean termo() {
        if (fator()) {
            next();
            if (op_multiplicativo()) {
                next();
                return termo();
            }
            return true;
        }

        Error("Erro no termo");
        return false;
    }

    private boolean fator() {
        if (simbolo.classificacao.equals(Dicionario.IDENTIFICADOR)) {
            next();

            if (simbolo.token.equals("(")) {
                next();

                if (lista_de_expressoes()) {
                    next();

                    if (simbolo.token.equals(")")) {
                        next();
                        return true;
                    }
                }
            } // (
            back();
            return true;
        } else if (simbolo.classificacao.equals(Dicionario.NUMERO_INTEIRO)) {
            return true;
        } else if (simbolo.classificacao.equals(Dicionario.NUMERO_REAL)) {
            return true;
        } else if (simbolo.token.equals("true")) {
            return true;
        } else if (simbolo.token.equals("false")) {
            return true;
        } else if (simbolo.token.equals("(")) {
            next();
            if (expressao()) {
                return simbolo.token.equals(")");
            } else {
                Error("Erro no fator");
                return false;
            }

        } else if (simbolo.token.equals("not")) {
            next();
            return fator();
        } else {
            Error("Erro no fator");
            return false;
        }

    }// fim de fator

    private boolean sinal() {
        return simbolo.token.matches(Dicionario.operadoresAditivos);
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

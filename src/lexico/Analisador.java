package lexico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Analisador {
    
    List<Simbolo> tabela = new ArrayList<>();//tabela que sera gerada
    List<String> dados;//recebe o codigo
    String linha;//recebe linha por linha do codigo
    String[] palavras;//cada palavra separada por espaço sera guardado nesse array a cada iteraçao
    String palavra;
    int pfim,pcom;
    boolean coment = false;
    int ini;

    Dicionario dici = new Dicionario();

    int com = 0;

    public Analisador(List<String> dados){
        this.dados = dados;
    }

    public List Lexico(){

        for(int i=0;i<dados.size();i++){//cada elemento da Lista é uma linha inteira do codigo dado
        //for global

            linha = dados.get(i);//pega a linha por linha
            
            palavras = linha.trim().split("\\s+");//separa todas as palavras em um array

            for(int j=0;j<palavras.length;j++){
                ini = 0;
                palavra = palavras[j];

                //usar essas variaveis para pegar delimitadores 
                pfim = palavra.length();//delimita a substring
                pcom = palavra.length()-1;//onde começa a substring
                
                if(palavra.equals("{")){//caso em que a chaves esta separada
                    coment = true;
                     continue;
                 }

                 if(palavra.substring(0).equals("{")){//caso em que a chaves esta junta da palavra
                    coment  = true;
                    continue;
                 }

                if(palavra.equals("}")){
                    coment = false;
                    continue;
                }

                if(palavra.substring(pcom,pfim).equals("}")){
                        coment = false;
                        continue;
                }

                if(coment == true)
                    continue;
                //os codigos acima sao tratamentos de comentario


                else if(palavra.matches(dici.palavraReservada)){
                    tabela.add(new Simbolo(palavra,"Palavra reservada",i));
                    continue;
                }

                else if(palavra.matches(dici.atribuicao)){
                    tabela.add(new Simbolo(palavra,"Atribuição",i));
                    continue;
                }

                else if(palavra.matches(dici.identificador)){
                    tabela.add(new Simbolo(palavra,"Identificador",i));
                    continue;
                }

                else if(palavra.matches(dici.numerosInteiros)){
                    tabela.add(new Simbolo(palavra,"Numero inteiro",i));
                    continue;
                }

                else if(palavra.matches(dici.numerosReais)){
                    tabela.add(new Simbolo(palavra,"Numero real",i));
                    continue;
                }

                else if(palavra.matches(dici.delimitadores)){
                    tabela.add(new Simbolo(palavra,"Delimitador",i));
                    continue;
                }
                
                // verifica se tem delimitador, se tiver verifica a palavra que vem antes dele
                //se n tiver ja verifica a palavra
                else if(palavra.substring(pcom,pfim).matches(dici.delimitadores)){

                    for(int x=0; x<palavra.length()-1; x++){//verificar se tem outro delimitador, casos em que 
                        //as palavras estao juntas
                        if(palavra.substring(x,x+1).matches(dici.delimitadores)){

                            if(palavra.substring(0,x).matches(dici.palavraReservada)){
                                tabela.add(new Simbolo(palavra.substring(0,x-1),"palavra reservada",i));
                            }
    
                            else if(palavra.substring(0,x).matches(dici.numerosInteiros)){
                                tabela.add(new Simbolo(palavra.substring(0,pfim-2),"Numero inteiro",i));
                            }
    
                            else if(palavra.substring(0,x).matches(dici.numerosReais)){
                                tabela.add(new Simbolo(palavra.substring(0,pfim-2),"Numero real",i));
                            }

                            else if(palavra.substring(0,x).matches(dici.identificador)){
                                tabela.add(new Simbolo(palavra.substring(0,pfim-2),"identificador",i));
                            }
    
                                tabela.add(new Simbolo(palavra.substring(x,x+1),"Delimitdor",i));
                                ini = x+1;
                                break;
                        }//if que achou o segundo delimitador
                    }//if do for


                    if(palavra.substring(ini,pfim-2).matches(dici.palavraReservada)){
                            tabela.add(new Simbolo(palavra.substring(0,pfim-2),"palavra reservada",i));
                    }

                    else if(palavra.substring(ini,pfim-2).matches(dici.numerosInteiros)){
                        tabela.add(new Simbolo(palavra.substring(0,pfim-2),"Numero inteiro",i));
                    }

                    else if(palavra.substring(ini,pfim-2).matches(dici.numerosReais)){
                        tabela.add(new Simbolo(palavra.substring(0,pfim-2),"Numero real",i));
                    }

                    else if(palavra.substring(ini,pfim-2).matches(dici.identificador)){
                        tabela.add(new Simbolo(palavra.substring(0,pfim-2),"identificador",i));
                    }

                    tabela.add(new Simbolo(palavra.substring(pcom,pfim),"Delimitdor",i));
                    continue;
                }//caso que tem delimitador
                
                
            }//for pra cada palavra

        }//for gloabal

        return tabela;
    }
}

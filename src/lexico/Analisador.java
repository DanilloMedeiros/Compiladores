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
    boolean skip = false;
    int ini;
    String dummy;

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
                palavra = palavras[j];//pega uma palavra por vez
                dummy = "";//usado para atualizar a palavra tirando os comentarios que estiverem juntos

                //usar essas variaveis para pegar delimitadores 
                pfim = palavra.length();//delimita a substring
                pcom = palavra.length()-1;//onde começa a substring
                
                
                if(palavra.equals("{")){//caso em que a chaves esta separada
                    coment = true;
                     continue;
                 }

                
                if(palavra.equals("}")){
                    coment = false;
                    continue;
                }

                for(int t = 0; t < palavra.length(); t++){//verificar se tem { no começo ou meio de uma frase
                   
                    if(palavra.substring(t, t+1).equals("{")){
                       
                        dummy += palavra.substring(0,t);//quando encontra atualiza a var palavra
                        coment = true;
                    }

                    if(palavra.substring(t, t+1).equals("}")){
                        
                        dummy += palavra.substring(t+1,palavra.length());//quando encontra atualiza a var palavra
                        coment = false; 
                    }
                }

                if(coment)//enquanto o comentario nao for fechado, vai skipar tudo ate achar a }
                    continue;
                //os codigos acima sao tratamentos de comentario

                if(dummy.length() > 0){//atualiza a palavra tirando comentarios
                    palavra = dummy;
                    pfim = palavra.length();
                    pcom = palavra.length()-1;
                }
              

                //testes para palavras separadas
                if(palavra.matches(dici.numerosInteiros)){
                    tabela.add(new Simbolo(palavra,"Numero inteiro",i));
                    continue;
                }

                else if(palavra.matches(dici.numerosReais)){
                    tabela.add(new Simbolo(palavra,"Numero real",i));
                    continue;
                }

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
                
                
                else if(palavra.matches(dici.delimitadores)){
                    tabela.add(new Simbolo(palavra,"Delimitador",i));
                    continue;
                }

                
                // verifica se tem delimitador, se tiver verifica a palavra que vem antes dele
                //se n tiver ja verifica a palavra
                else if(palavra.substring(pcom,pfim).matches(dici.delimitadores)){

                    for(int x=0; x<palavra.length()-1; x++){//verificar se tem outro delimitador, casos em que 
                        //as palavras estao juntas
                        if(palavra.substring(x,x+1).matches(dici.delimitadores)){//acha o :
                            if(palavra.substring(x,x+2).matches(dici.atribuicao)){//verifica se depois tem =
                                
                                tabela.add(new Simbolo(palavra.substring(0,x),"identificador",i));
                                  
                                tabela.add(new Simbolo(palavra.substring(x,x+2),"Atribuição",i));
                                ini = x + 2;
                                break;

                            }//if da atribuição

                            //verifica qual palavra vem antes do segundo delimitador
                            if(palavra.substring(0,x).matches(dici.palavraReservada)){
                                tabela.add(new Simbolo(palavra.substring(0,x),"palavra reservada",i));
                            }
    
                            else if(palavra.substring(0,x).matches(dici.numerosInteiros)){
                                tabela.add(new Simbolo(palavra.substring(0,x),"Numero inteiro",i));
                            }
    
                            else if(palavra.substring(0,x).matches(dici.numerosReais)){
                                tabela.add(new Simbolo(palavra.substring(0,x),"Numero real",i));
                            }

                            else if(palavra.substring(0,x).matches(dici.identificador)){
                                tabela.add(new Simbolo(palavra.substring(0,x),"identificador",i));
                            }
                                
                            tabela.add(new Simbolo(palavra.substring(x,x+1),"Delimitdor",i));//adiciona o segundo delim
                            ini = x+1;
                            break;
                                
                        }//if que achou o segundo delimitador
                    }//if do for
                    
                    //verifica a palavra antes do delimitador final
                    if(palavra.substring(ini,pfim-1).matches(dici.palavraReservada)){
                            tabela.add(new Simbolo(palavra.substring(ini,pfim-1),"palavra reservada",i));
                    }

                    else if(palavra.substring(ini,pfim-1).matches(dici.numerosInteiros)){
                        tabela.add(new Simbolo(palavra.substring(ini,pfim-1),"Numero inteiro",i));
                    }

                    else if(palavra.substring(ini,pfim-1).matches(dici.numerosReais)){
                        tabela.add(new Simbolo(palavra.substring(ini,pfim-1),"Numero real",i));
                    }

                    else if(palavra.substring(ini,pfim-1).matches(dici.identificador)){
                        tabela.add(new Simbolo(palavra.substring(ini,pfim-1),"identificador",i));
                    }

                    tabela.add(new Simbolo(palavra.substring(pcom,pfim),"Delimitdor",i));//add o delimitador
                    continue;
                }//caso que tem delimitador
                
                if(palavra.contains("{")){//caso em que eh um comentario separado
                    continue;
                    
                }else{//caso em que termina em numero ou atribuiçao
                    for(int k=0;k<palavra.length();k++){//caso que tem atribuiçao sem delimitador
                        if(palavra.substring(k,k+2).matches(dici.atribuicao)){

                            tabela.add(new Simbolo(palavra.substring(0,k),"Identificador",i));
                            tabela.add(new Simbolo(palavra.substring(k,k+2),"Atribuição",i));
                            if(k+2 < palavra.length()){//existe algo depois da atribuiçao i:=1

                                if(palavra.substring(k+2,palavra.length()).matches(dici.numerosInteiros)){
                                    tabela.add(new Simbolo(palavra.substring(k+2,palavra.length()),"Numero inteiro",i));
                                }

                                if(palavra.substring(k+2,palavra.length()).matches(dici.numerosReais)){
                                    tabela.add(new Simbolo(palavra.substring(k+2,palavra.length()),"Numero Real",i));
                                }
                                    break;
                            }else{
                                break;
                        }
                                      
                    }//for atribuiçao
                }
            }
                
            }//for pra cada palavra
        }//for gloabal
        return tabela;
    }
}

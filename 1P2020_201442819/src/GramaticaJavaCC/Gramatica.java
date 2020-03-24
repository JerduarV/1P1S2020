/* Gramatica.java */
/* Generated By:JavaCC: Do not edit this line. Gramatica.java */
/** Analizador de Lenguaje Arit en JAVACC */

package GramaticaJavaCC;

import Interprete.Arbol;
import Interprete.Expresiones.*;
import Interprete.Expresiones.Operaciones.*;
import Interprete.Instrucciones.*;
import java.util.LinkedList;
import Interprete.NodoAST;

public class Gramatica implements GramaticaConstants {

    /**
     * Función para procesar la cadena
    */
    private Expresion procesarCadena(String cad, int fila, int col){
        cad = cad.substring(1,cad.length()-1);
        cad = cad.replace("\u005c\u005c","\u005c\u005c");
        cad = cad.replace("\u005c\u005cn","\u005cn");
        cad = cad.replace("\u005c\u005ct","\u005ct");
        return new Primitivo(fila, col, cad);
    }

/** Fin Lexico */

/** Producción inicial 
    INI -> L_INSTR
*/
  final public Arbol INI() throws ParseException {LinkedList<NodoAST> l;
    l = L_INSTR();
    jj_consume_token(0);
Arbol a = new Arbol(l); {if ("" != null) return a;}
    throw new Error("Missing return statement in function");
  }

/**
    L_INSTR ->  L_INSTR INSTR
            |   INSTR
*/
  final public LinkedList<NodoAST> L_INSTR() throws ParseException {NodoAST i;
    LinkedList<NodoAST> l = new LinkedList<NodoAST>();
    label_1:
    while (true) {
      i = INSTR();
l.add(i);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case RIF:
      case RWHILE:
      case RDO:
      case RFOR:
      case RRETURN:
      case IDENTIFICADOR:{
        ;
        break;
        }
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
    }
{if ("" != null) return l;}
    throw new Error("Missing return statement in function");
  }

/**
    BLOQUE ->   '{' L_INSTR '}'
            |   '{' '}'
*/
  final public LinkedList<NodoAST> BLOQUE() throws ParseException {LinkedList<NodoAST> l = new LinkedList<NodoAST>();
    jj_consume_token(LLAVEIZQ);
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case RIF:
    case RWHILE:
    case RDO:
    case RFOR:
    case RRETURN:
    case IDENTIFICADOR:{
      l = L_INSTR();
      break;
      }
    default:
      jj_la1[1] = jj_gen;
      ;
    }
    jj_consume_token(LLAVEDER);
{if ("" != null) return l;}
    throw new Error("Missing return statement in function");
  }

/** 
    INSTR ->    DEC_FUN
            |   CALL_FUN
            |   ASIGNACION
            |   CALL_FUN
            |   RETURN
            |   IF
            |   WHILE
*/
  final public NodoAST INSTR() throws ParseException {NodoAST i;
    if (jj_2_1(3)) {
      i = DEC_FUN();
{if ("" != null) return i;}
    } else if (jj_2_2(2)) {
      i = CALL_FUN();
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case PTCOMA:{
        jj_consume_token(PTCOMA);
        break;
        }
      default:
        jj_la1[2] = jj_gen;
        ;
      }
{if ("" != null) return i;}
    } else {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IDENTIFICADOR:{
        i = ASIGNACION();
{if ("" != null) return i;}
        break;
        }
      case RRETURN:{
        i = RETURN();
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case PTCOMA:{
          jj_consume_token(PTCOMA);
          break;
          }
        default:
          jj_la1[3] = jj_gen;
          ;
        }
{if ("" != null) return i;}
        break;
        }
      case RIF:{
        i = IF();
{if ("" != null) return i;}
        break;
        }
      case RWHILE:{
        i = WHILE();
{if ("" != null) return i;}
        break;
        }
      case RDO:{
        i = DOWHILE();
{if ("" != null) return i;}
        break;
        }
      case RFOR:{
        i = FOR();
{if ("" != null) return i;}
        break;
        }
      default:
        jj_la1[4] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    throw new Error("Missing return statement in function");
  }

/**
    FOR ->  'FOR' '(' ID 'IN' EXP ')' BLOQUE
*/
  final public NodoAST FOR() throws ParseException {Token id;
    LinkedList<NodoAST> b;
    Expresion e;
    jj_consume_token(RFOR);
    jj_consume_token(PARIZQ);
    id = jj_consume_token(IDENTIFICADOR);
    jj_consume_token(RIN);
    e = EXP();
    jj_consume_token(PARDER);
    b = BLOQUE();
{if ("" != null) return new For(id.image,e,b,token.beginLine,token.beginColumn);}
    throw new Error("Missing return statement in function");
  }

/**
    DOWHILE ->  'DO' BLOQUE '(' EXP ')' ( ';' )?
*/
  final public NodoAST DOWHILE() throws ParseException {Expresion e;
    LinkedList<NodoAST> b;
    jj_consume_token(RDO);
    b = BLOQUE();
    jj_consume_token(RWHILE);
    jj_consume_token(PARIZQ);
    e = EXP();
    jj_consume_token(PARDER);
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case PTCOMA:{
      jj_consume_token(PTCOMA);
      break;
      }
    default:
      jj_la1[5] = jj_gen;
      ;
    }
{if ("" != null) return new DoWhile(b,e,token.beginLine,token.beginColumn);}
    throw new Error("Missing return statement in function");
  }

/**
    WHILE ->    'WHILE' '(' EXP ')' BLOQUE
*/
  final public NodoAST WHILE() throws ParseException {Expresion e;
    LinkedList<NodoAST> l;
    jj_consume_token(RWHILE);
    jj_consume_token(PARIZQ);
    e = EXP();
    jj_consume_token(PARDER);
    l = BLOQUE();
{if ("" != null) return new While(e,l,token.beginLine,token.beginColumn);}
    throw new Error("Missing return statement in function");
  }

/**
    IF -> 'IF' '(' EXP ')' ( ELSE )?
*/
  final public NodoAST IF() throws ParseException {Expresion e;
    Instruccion i;
    LinkedList<NodoAST> c;
    NodoAST n;
    jj_consume_token(RIF);
    jj_consume_token(PARIZQ);
    e = EXP();
    jj_consume_token(PARDER);
    c = BLOQUE();
n = new Si(e,c,token.beginLine,token.beginColumn);
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case RELSE:{
      i = ELSE();
n = new Si(e,c,(Else)i,token.beginLine,token.beginColumn);
      break;
      }
    default:
      jj_la1[6] = jj_gen;
      ;
    }
{if ("" != null) return n;}
    throw new Error("Missing return statement in function");
  }

/**
    ELSE ->     'ELSE' BLOQUE
            |   'ELSE' IF
*/
  final public Instruccion ELSE() throws ParseException {LinkedList<NodoAST> l;
    NodoAST si;
    jj_consume_token(RELSE);
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case RIF:{
      si = IF();
{if ("" != null) return new Else((Si)si,token.beginLine,token.beginColumn);}
      break;
      }
    case LLAVEIZQ:{
      l = BLOQUE();
{if ("" != null) return new Else(l,token.beginLine,token.beginColumn);}
      break;
      }
    default:
      jj_la1[7] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

/**
    RETURN ->   RRETURN '(' EXP ')'
            |   RRETURN
*/
  final public Expresion RETURN() throws ParseException {Expresion e;
    if (jj_2_3(2)) {
      jj_consume_token(RRETURN);
      jj_consume_token(PARIZQ);
      e = EXP();
      jj_consume_token(PARDER);
{if ("" != null) return new Return(e, token.beginLine, token.beginColumn);}
    } else {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case RRETURN:{
        jj_consume_token(RRETURN);
{if ("" != null) return new Return(token.beginLine, token.beginColumn);}
        break;
        }
      default:
        jj_la1[8] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    throw new Error("Missing return statement in function");
  }

/**
    DEC_FUN -> IDENFICADOR IGUAL 'FUNCTION' '(' (L_PARAM_FORM)? ')' '{' BLOQUE '}'
*/
  final public Instruccion DEC_FUN() throws ParseException {LinkedList<Declaracion> l = new LinkedList<Declaracion>();
    LinkedList<NodoAST> b = new LinkedList<NodoAST>();
    Token t;
    t = jj_consume_token(IDENTIFICADOR);
    jj_consume_token(IGUAL);
    jj_consume_token(RFUNCTION);
    jj_consume_token(PARIZQ);
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case IDENTIFICADOR:{
      l = L_PARAM_FORM();
      break;
      }
    default:
      jj_la1[9] = jj_gen;
      ;
    }
    jj_consume_token(PARDER);
    b = BLOQUE();
{if ("" != null) return new DecFuncion(t.image, l, b, t.beginLine, t.beginColumn);}
    throw new Error("Missing return statement in function");
  }

  final public LinkedList<Declaracion> L_PARAM_FORM() throws ParseException {Declaracion d;
    LinkedList<Declaracion> l = new LinkedList<Declaracion>();
    d = PARAM_FORM();
l.add(d);
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case COMA:{
        ;
        break;
        }
      default:
        jj_la1[10] = jj_gen;
        break label_2;
      }
      jj_consume_token(COMA);
      d = PARAM_FORM();
l.add(d);
    }
{if ("" != null) return l;}
    throw new Error("Missing return statement in function");
  }

  final public Declaracion PARAM_FORM() throws ParseException {Token t;
    Expresion e;
    if (jj_2_4(2)) {
      t = jj_consume_token(IDENTIFICADOR);
      jj_consume_token(IGUAL);
      e = EXP();
{if ("" != null) return new Declaracion(t.image, e, t.beginLine, t.beginColumn);}
    } else {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IDENTIFICADOR:{
        t = jj_consume_token(IDENTIFICADOR);
{if ("" != null) return new Declaracion(t.image, t.beginLine, t.beginColumn);}
        break;
        }
      default:
        jj_la1[11] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    throw new Error("Missing return statement in function");
  }

/**
    ASIGNACION ->   IDENTIFICADOR IGUAL EXP (PTCOMA)?
*/
  final public Instruccion ASIGNACION() throws ParseException {Token id;
    Expresion e;
    LinkedList<Indice> l = new LinkedList<Indice>();
    if (jj_2_5(2)) {
      id = jj_consume_token(IDENTIFICADOR);
      l = L_INDEX();
      jj_consume_token(IGUAL);
      e = EXP();
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case PTCOMA:{
        jj_consume_token(PTCOMA);
        break;
        }
      default:
        jj_la1[12] = jj_gen;
        ;
      }
{if ("" != null) return new AccesoAsig(l,new Identificador(id.image,id.beginLine,id.beginColumn),e,id.beginLine,id.beginColumn);}
    } else {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IDENTIFICADOR:{
        id = jj_consume_token(IDENTIFICADOR);
        jj_consume_token(IGUAL);
        e = EXP();
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case PTCOMA:{
          jj_consume_token(PTCOMA);
          break;
          }
        default:
          jj_la1[13] = jj_gen;
          ;
        }
{if ("" != null) return new Asignacion(token.beginLine,token.beginColumn,id.image,e);}
        break;
        }
      default:
        jj_la1[14] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    throw new Error("Missing return statement in function");
  }

/** 
    EXP -> EXP_AND ( <OR> EXP_AND )*
*/
  final public Expresion EXP() throws ParseException {Expresion e, e1;
    e = EXP_AND();
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case OR:{
        ;
        break;
        }
      default:
        jj_la1[15] = jj_gen;
        break label_3;
      }
      jj_consume_token(OR);
      e1 = EXP_AND();
e = new ExpLogica(token.beginLine, token.beginColumn, e, e1, TipoOpe.OR);
    }
{if ("" != null) return e;}
    throw new Error("Missing return statement in function");
  }

/**
    EXP_AND -> EXP_IGUAL ( <AND> EXP_IGUAL )*
*/
  final public Expresion EXP_AND() throws ParseException {Expresion e, e1;
    e = EXP_IGUAL();
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case AND:{
        ;
        break;
        }
      default:
        jj_la1[16] = jj_gen;
        break label_4;
      }
      jj_consume_token(AND);
      e1 = EXP_IGUAL();
e = new ExpLogica(token.beginLine, token.beginColumn, e, e1, TipoOpe.AND);
    }
{if ("" != null) return e;}
    throw new Error("Missing return statement in function");
  }

/**
    EXP_IGUAL ->    EXP_REL ( == EXP_REL )*
               |    EXP_REL ( != EXP_REL )*
*/
  final public Expresion EXP_IGUAL() throws ParseException {Expresion e, e1;
    e = EXP_REL();
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IGUALQUE:
      case DIFERENTE:{
        ;
        break;
        }
      default:
        jj_la1[17] = jj_gen;
        break label_5;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IGUALQUE:{
        jj_consume_token(IGUALQUE);
        e1 = EXP_REL();
e = new ExpRelacional(token.beginLine, token.beginColumn, e, e1, TipoOpe.IGUALQUE);
        break;
        }
      case DIFERENTE:{
        jj_consume_token(DIFERENTE);
        e1 = EXP_REL();
e = new ExpRelacional(token.beginLine, token.beginColumn, e, e1, TipoOpe.DIFERENTE);
        break;
        }
      default:
        jj_la1[18] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
{if ("" != null) return e;}
    throw new Error("Missing return statement in function");
  }

/**
    EXP_REL ->  EXP_ADD ( > EXP_ADD )*
            |   EXP_ADD ( < EXP_ADD )*
            |   EXP_ADD ( <= EXP_ADD )*
            |   EXP_ADD ( >= EXP_ADD )*
*/
  final public Expresion EXP_REL() throws ParseException {Expresion e, e1;
    e = EXP_ADD();
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case MENOR:
      case MAYOR:
      case MENORIGUAL:
      case MAYORIGUAL:{
        ;
        break;
        }
      default:
        jj_la1[19] = jj_gen;
        break label_6;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case MAYOR:{
        jj_consume_token(MAYOR);
        e1 = EXP_ADD();
e = new ExpRelacional(token.beginLine, token.beginColumn, e, e1, TipoOpe.MAYOR);
        break;
        }
      case MENOR:{
        jj_consume_token(MENOR);
        e1 = EXP_ADD();
e = new ExpRelacional(token.beginLine, token.beginColumn, e, e1, TipoOpe.MENOR);
        break;
        }
      case MENORIGUAL:{
        jj_consume_token(MENORIGUAL);
        e1 = EXP_ADD();
e = new ExpRelacional(token.beginLine, token.beginColumn, e, e1, TipoOpe.MENORIGUAL);
        break;
        }
      case MAYORIGUAL:{
        jj_consume_token(MAYORIGUAL);
        e1 = EXP_ADD();
e = new ExpRelacional(token.beginLine, token.beginColumn, e, e1, TipoOpe.MAYORIGUAL);
        break;
        }
      default:
        jj_la1[20] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
{if ("" != null) return e;}
    throw new Error("Missing return statement in function");
  }

/**
    EXP_ADD ->  EXP_MULT ( + EXP_MULT )*
            |   EXP_MULT ( - EXP_MULT )*
*/
  final public Expresion EXP_ADD() throws ParseException {Expresion e, e1;
    e = EXP_MULT();
    label_7:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case MAS:
      case MENOS:{
        ;
        break;
        }
      default:
        jj_la1[21] = jj_gen;
        break label_7;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case MAS:{
        jj_consume_token(MAS);
        e1 = EXP_MULT();
e = new ExpAritmetica(token.beginLine, token.beginColumn, e, e1, TipoOpe.SUMA);
        break;
        }
      case MENOS:{
        jj_consume_token(MENOS);
        e1 = EXP_MULT();
e = new ExpAritmetica(token.beginLine, token.beginColumn, e, e1, TipoOpe.RESTA);
        break;
        }
      default:
        jj_la1[22] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
{if ("" != null) return e;}
    throw new Error("Missing return statement in function");
  }

/** 
    EXP_MULT ->     EXP_POT ( * EXP_POT )*
            |       EXP_POT ( / EXP_POT )*
*/
  final public Expresion EXP_MULT() throws ParseException {Expresion e, e1;
    e = EXP_POT();
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case POR:
      case DIV:
      case MOD:{
        ;
        break;
        }
      default:
        jj_la1[23] = jj_gen;
        break label_8;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case POR:{
        jj_consume_token(POR);
        e1 = EXP_POT();
e = new ExpAritmetica(token.beginLine, token.beginColumn, e, e1, TipoOpe.MULT);
        break;
        }
      case DIV:{
        jj_consume_token(DIV);
        e1 = EXP_POT();
e = new ExpAritmetica(token.beginLine, token.beginColumn, e, e1, TipoOpe.DIV);
        break;
        }
      case MOD:{
        jj_consume_token(MOD);
        e1 = EXP_POT();
e = new ExpAritmetica(token.beginLine, token.beginColumn, e, e1, TipoOpe.MOD);
        break;
        }
      default:
        jj_la1[24] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
{if ("" != null) return e;}
    throw new Error("Missing return statement in function");
  }

/**
    EXP_POT ->  EXP_U ( ^ EXP_U )*
*/
  final public Expresion EXP_POT() throws ParseException {Expresion e, e1;
    e = EXP_A();
    label_9:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case POT:{
        ;
        break;
        }
      default:
        jj_la1[25] = jj_gen;
        break label_9;
      }
      jj_consume_token(POT);
      e1 = EXP_A();
e = new ExpAritmetica(token.beginLine, token.beginColumn, e, e1, TipoOpe.POT);
    }
{if ("" != null) return e;}
    throw new Error("Missing return statement in function");
  }

  final public Expresion EXP_A() throws ParseException {Expresion e;
    LinkedList<Indice> l;
    e = EXP_U();
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case CORIZQ:{
      l = L_INDEX();
e = new AccesoGet(e,l);
      break;
      }
    default:
      jj_la1[26] = jj_gen;
      ;
    }
{if ("" != null) return e;}
    throw new Error("Missing return statement in function");
  }

/**
    L_INDEX ->  L_INDEX INDEX
            |   INDEX
*/
  final public LinkedList<Indice> L_INDEX() throws ParseException {LinkedList<Indice> l = new LinkedList<Indice>();
    Expresion e, e1;
    Indice i;
    label_10:
    while (true) {
      if (jj_2_6(2)) {
        jj_consume_token(CORIZQ);
        jj_consume_token(CORIZQ);
        e = EXP();
        jj_consume_token(CORDER);
        jj_consume_token(CORDER);
l.add(new Indice(e, Indice.TipoIndice.DOBLE));
      } else if (jj_2_7(2)) {
        jj_consume_token(CORIZQ);
        jj_consume_token(COMA);
        e = EXP();
        jj_consume_token(CORDER);
l.add(new Indice(e,Indice.TipoIndice.MATRIX_COL));
      } else {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case CORIZQ:{
          jj_consume_token(CORIZQ);
          e = EXP();
i = new Indice(e);
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case COMA:{
            jj_consume_token(COMA);
i = new Indice(e, Indice.TipoIndice.MATRIX_ROW);
            switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
            case NUMERO:
            case DECIMAL:
            case RTRUE:
            case RFALSE:
            case PARIZQ:
            case MENOS:
            case NOT:
            case IDENTIFICADOR:
            case STRING:{
              e1 = EXP();
i = new Indice(e,e1);
              break;
              }
            default:
              jj_la1[27] = jj_gen;
              ;
            }
            break;
            }
          default:
            jj_la1[28] = jj_gen;
            ;
          }
          jj_consume_token(CORDER);
l.add(i);
          break;
          }
        default:
          jj_la1[29] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case CORIZQ:{
        ;
        break;
        }
      default:
        jj_la1[30] = jj_gen;
        break label_10;
      }
    }
{if ("" != null) return l;}
    throw new Error("Missing return statement in function");
  }

/** 
    EXP_U ->    - EXP_U
            |   ! EXP_U
            |   PRIMITIVO
*/
  final public Expresion EXP_U() throws ParseException {Expresion e;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case MENOS:{
      jj_consume_token(MENOS);
      e = EXP_U();
{if ("" != null) return new ExpAritmetica(e, token.beginLine, token.beginColumn);}
      break;
      }
    case NOT:{
      jj_consume_token(NOT);
      e = EXP_U();
{if ("" != null) return new ExpLogica(e, token.beginLine, token.beginColumn);}
      break;
      }
    case NUMERO:
    case DECIMAL:
    case RTRUE:
    case RFALSE:
    case PARIZQ:
    case IDENTIFICADOR:
    case STRING:{
      e = PRIMITIVO();
{if ("" != null) return e;}
      break;
      }
    default:
      jj_la1[31] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

/** Primitivo -> Numero
              |  Decimal
              |  '(' Expresion ')' 
*/
  final public Expresion PRIMITIVO() throws ParseException {Expresion e;
    String s;
    Token t;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case NUMERO:{
      jj_consume_token(NUMERO);
{if ("" != null) return new Primitivo(token.beginLine, token.beginColumn, Integer.parseInt(token.image));}
      break;
      }
    case DECIMAL:{
      jj_consume_token(DECIMAL);
{if ("" != null) return new Primitivo(token.beginLine, token.beginColumn, Double.parseDouble(token.image));}
      break;
      }
    case RTRUE:{
      jj_consume_token(RTRUE);
{if ("" != null) return new Primitivo(token.beginLine, token.beginColumn, true);}
      break;
      }
    case RFALSE:{
      jj_consume_token(RFALSE);
{if ("" != null) return new Primitivo(token.beginLine, token.beginColumn, false);}
      break;
      }
    default:
      jj_la1[32] = jj_gen;
      if (jj_2_8(2)) {
        e = CALL_FUN();
{if ("" != null) return e;}
      } else {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case STRING:{
          t = jj_consume_token(STRING);
{if ("" != null) return this.procesarCadena(t.image, token.beginLine, token.beginColumn);}
          break;
          }
        case PARIZQ:{
          jj_consume_token(PARIZQ);
          e = EXP();
          jj_consume_token(PARDER);
{if ("" != null) return e;}
          break;
          }
        default:
          jj_la1[33] = jj_gen;
          if (jj_2_9(2)) {
            t = jj_consume_token(IDENTIFICADOR);
{if ("" != null) return new Identificador(t.image, token.beginLine,token.beginColumn);}
          } else {
            jj_consume_token(-1);
            throw new ParseException();
          }
        }
      }
    }
    throw new Error("Missing return statement in function");
  }

/**
    CALL_FUN -> IDENTIFICADOR PARIZQ LISTA_EXP PARDER
            |   IDENTIFICADOR PARIZQ PARDER
*/
  final public Expresion CALL_FUN() throws ParseException {LinkedList<Expresion> l;
    Token t;
    t = jj_consume_token(IDENTIFICADOR);
    jj_consume_token(PARIZQ);
    l = L_PARAM_ACT();
    jj_consume_token(PARDER);
{if ("" != null) return CallFun.ReturnCallFun(t.image,l,token.beginLine,token.beginColumn);}
    throw new Error("Missing return statement in function");
  }

/**
    L_PARAM_ACT ->  L_PARAM_ACT PARAM_ACT
                |   PARAM_ACT
*/
  final public LinkedList<Expresion> L_PARAM_ACT() throws ParseException {LinkedList<Expresion> l = new LinkedList<Expresion>();
    Expresion e;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case NUMERO:
    case DECIMAL:
    case RTRUE:
    case RFALSE:
    case RDEFAULT:
    case PARIZQ:
    case MENOS:
    case NOT:
    case IDENTIFICADOR:
    case STRING:{
      e = PARAM_ACT();
l.add(e);
      label_11:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case COMA:{
          ;
          break;
          }
        default:
          jj_la1[34] = jj_gen;
          break label_11;
        }
        jj_consume_token(COMA);
        e = PARAM_ACT();
l.add(e);
      }
      break;
      }
    default:
      jj_la1[35] = jj_gen;
      ;
    }
{if ("" != null) return l;}
    throw new Error("Missing return statement in function");
  }

/**
    PARAM_ACT ->    EXP
                |   'DEFAULT'
*/
  final public Expresion PARAM_ACT() throws ParseException {Expresion e;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case NUMERO:
    case DECIMAL:
    case RTRUE:
    case RFALSE:
    case PARIZQ:
    case MENOS:
    case NOT:
    case IDENTIFICADOR:
    case STRING:{
      e = EXP();
{if ("" != null) return e;}
      break;
      }
    case RDEFAULT:{
      jj_consume_token(RDEFAULT);
{if ("" != null) return new ValorDefault(token.beginLine, token.beginColumn);}
      break;
      }
    default:
      jj_la1[36] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  private boolean jj_2_1(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  private boolean jj_2_2(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  private boolean jj_2_3(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(2, xla); }
  }

  private boolean jj_2_4(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_4(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(3, xla); }
  }

  private boolean jj_2_5(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_5(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(4, xla); }
  }

  private boolean jj_2_6(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_6(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(5, xla); }
  }

  private boolean jj_2_7(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_7(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(6, xla); }
  }

  private boolean jj_2_8(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_8(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(7, xla); }
  }

  private boolean jj_2_9(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_9(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(8, xla); }
  }

  private boolean jj_3_2()
 {
    if (jj_3R_13()) return true;
    return false;
  }

  private boolean jj_3_3()
 {
    if (jj_scan_token(RRETURN)) return true;
    if (jj_scan_token(PARIZQ)) return true;
    return false;
  }

  private boolean jj_3R_16()
 {
    if (jj_scan_token(CORIZQ)) return true;
    return false;
  }

  private boolean jj_3_1()
 {
    if (jj_3R_12()) return true;
    return false;
  }

  private boolean jj_3_7()
 {
    if (jj_scan_token(CORIZQ)) return true;
    if (jj_scan_token(COMA)) return true;
    return false;
  }

  private boolean jj_3R_15()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_6()) {
    jj_scanpos = xsp;
    if (jj_3_7()) {
    jj_scanpos = xsp;
    if (jj_3R_16()) return true;
    }
    }
    return false;
  }

  private boolean jj_3_6()
 {
    if (jj_scan_token(CORIZQ)) return true;
    if (jj_scan_token(CORIZQ)) return true;
    return false;
  }

  private boolean jj_3_9()
 {
    if (jj_scan_token(IDENTIFICADOR)) return true;
    return false;
  }

  private boolean jj_3R_12()
 {
    if (jj_scan_token(IDENTIFICADOR)) return true;
    if (jj_scan_token(IGUAL)) return true;
    if (jj_scan_token(RFUNCTION)) return true;
    return false;
  }

  private boolean jj_3_4()
 {
    if (jj_scan_token(IDENTIFICADOR)) return true;
    if (jj_scan_token(IGUAL)) return true;
    return false;
  }

  private boolean jj_3_8()
 {
    if (jj_3R_13()) return true;
    return false;
  }

  private boolean jj_3R_14()
 {
    Token xsp;
    if (jj_3R_15()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_15()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3R_13()
 {
    if (jj_scan_token(IDENTIFICADOR)) return true;
    if (jj_scan_token(PARIZQ)) return true;
    return false;
  }

  private boolean jj_3_5()
 {
    if (jj_scan_token(IDENTIFICADOR)) return true;
    if (jj_3R_14()) return true;
    return false;
  }

  /** Generated Token Manager. */
  public GramaticaTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  private int jj_gen;
  final private int[] jj_la1 = new int[37];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x9c1000,0x9c1000,0x80000000,0x80000000,0x9c1000,0x80000000,0x2000,0x10001000,0x800000,0x0,0x40000000,0x0,0x80000000,0x80000000,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x4000d80,0x40000000,0x0,0x0,0x4000d80,0xd80,0x4000000,0x40000000,0x5000d80,0x5000d80,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x100000,0x100000,0x0,0x0,0x100000,0x0,0x0,0x0,0x0,0x100000,0x0,0x100000,0x0,0x0,0x100000,0x20000,0x10000,0xc000,0xc000,0x3c00,0x3c00,0x30,0x30,0x2c0,0x2c0,0x100,0x2,0x940020,0x0,0x2,0x2,0x940020,0x0,0x800000,0x0,0x940020,0x940020,};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[9];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  /** Constructor with InputStream. */
  public Gramatica(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Gramatica(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new GramaticaTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 37; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 37; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public Gramatica(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new GramaticaTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 37; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 37; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public Gramatica(GramaticaTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 37; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(GramaticaTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 37; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  @SuppressWarnings("serial")
  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk_f() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              continue jj_entries_loop;
            }
          }
          jj_expentries.add(jj_expentry);
          break jj_entries_loop;
        }
      }
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[56];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 37; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 56; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

  private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 9; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
            case 2: jj_3_3(); break;
            case 3: jj_3_4(); break;
            case 4: jj_3_5(); break;
            case 5: jj_3_6(); break;
            case 6: jj_3_7(); break;
            case 7: jj_3_8(); break;
            case 8: jj_3_9(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}

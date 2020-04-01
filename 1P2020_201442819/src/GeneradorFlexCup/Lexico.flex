/*
 * Desarrollado por Jerson Villatoro
 */
package GramaticaFlexCup;
import java_cup.runtime.Symbol;
import Editor.VentanaErrores; 

%% 
%class Lexico
%public 
%line
%column
%cup 
%unicode
%ignorecase

%{
    StringBuffer string = new StringBuffer();
%}

%init{ 
    yyline = 1; 
    yycolumn = 1;
%init} 
 
BLANCOS =               [ \r\t]+
ID  =                   ([A-Za-z]+[".""_"0-9A-Za-z]*)|("."+([A-Za-z][".""_"0-9A-Za-z]*)?)
D   =                   [0-9]+
DD  =                   [0-9]+("."[0-9]+)
COMENTUNILINEA =        ("#".*\r\n)|("#".*\n)|("#".*\r)
COMENTMULTILINEA =      "#*""#"*([^*#]|[^*]"#"|"*"[^#])*"*"*"*#"
INICIACADENA =          "\""

%state STRING1

%%

<YYINITIAL>{

{COMENTUNILINEA}        {} 
{COMENTMULTILINEA}      {}  

//PALABRAS RESERVADAS

"null"                  {return new Symbol(sym.RNULL,yyline,yycolumn,yytext());}
"true"                  {return new Symbol(sym.RTRUE,yyline,yycolumn,yytext());}
"false"                 {return new Symbol(sym.RFALSE,yyline,yycolumn,yytext());}
"if"                    {return new Symbol(sym.RIF,yyline,yycolumn,yytext());}
"else"                  {return new Symbol(sym.RELSE,yyline,yycolumn,yytext());}
"switch"                {return new Symbol(sym.RSWITCH,yyline,yycolumn,yytext());}
"case"                  {return new Symbol(sym.RCASE,yyline,yycolumn,yytext());}
"break"                 {return new Symbol(sym.RBREAK,yyline,yycolumn,yytext());}
"continue"              {return new Symbol(sym.RCONTINUE,yyline,yycolumn,yytext());}
"while"                 {return new Symbol(sym.RWHILE,yyline,yycolumn,yytext());}
"do"                    {return new Symbol(sym.RDO,yyline,yycolumn,yytext());}
"for"                   {return new Symbol(sym.RFOR,yyline,yycolumn,yytext());}
"in"                    {return new Symbol(sym.RIN,yyline,yycolumn,yytext());}
"function"              {return new Symbol(sym.RFUNCTION,yyline,yycolumn,yytext());}
"return"                {return new Symbol(sym.RRETURN,yyline,yycolumn,yytext());}
"default"               {return new Symbol(sym.RDEFAULT,yyline,yycolumn,yytext());}
 


"="                     {return new Symbol(sym.IGUAL,yyline,yycolumn,yytext());}
"("                     {return new Symbol(sym.PARIZQ,yyline,yycolumn,yytext());} 
")"                     {return new Symbol(sym.PARDER,yyline,yycolumn,yytext());} 
"{"                     {return new Symbol(sym.LLAVEIZQ,yyline,yycolumn,yytext());} 
"}"                     {return new Symbol(sym.LLAVEDER,yyline,yycolumn,yytext());}
","                     {return new Symbol(sym.COMA,yyline,yycolumn,yytext());}
";"                     {return new Symbol(sym.PTCOMA,yyline,yycolumn,yytext());}
":"                     {return new Symbol(sym.DOSPUNTOS,yyline,yycolumn,yytext());}
"["                     {return new Symbol(sym.CORIZQ,yyline,yycolumn,yytext());}
"]"                     {return new Symbol(sym.CORDER,yyline,yycolumn,yytext());}
"=>"                    {return new Symbol(sym.FLECHA,yyline,yycolumn,yytext());}

//OPERADORES
"+"                     {return new Symbol(sym.MAS,yyline,yycolumn,yytext());}
"-"                     {return new Symbol(sym.MENOS,yyline,yycolumn,yytext());}
"*"                     {return new Symbol(sym.POR,yyline,yycolumn,yytext());}
"/"                     {return new Symbol(sym.DIV,yyline,yycolumn,yytext());}
"^"                     {return new Symbol(sym.POT,yyline,yycolumn,yytext());}
"%%"                    {return new Symbol(sym.MOD,yyline,yycolumn,yytext());}

"<"                     {return new Symbol(sym.MENOR,yyline,yycolumn,yytext());}
">"                     {return new Symbol(sym.MAYOR,yyline,yycolumn,yytext());}
"<="                    {return new Symbol(sym.MENORIGUAL,yyline,yycolumn,yytext());}
">="                    {return new Symbol(sym.MAYORIGUAL,yyline,yycolumn,yytext());}
"=="                    {return new Symbol(sym.IGUALQUE,yyline,yycolumn,yytext());}
"!="                    {return new Symbol(sym.DIFERENTE,yyline,yycolumn,yytext());}

"&"                     {return new Symbol(sym.AND,yyline,yycolumn,yytext());}
"|"                     {return new Symbol(sym.OR,yyline,yycolumn,yytext());}
"!"                     {return new Symbol(sym.NOT,yyline,yycolumn,yytext());}

"?"                     {return new Symbol(sym.TERNARIO,yyline,yycolumn,yytext());}
//FIN OPERADORES

//DATOS PRIMITIVOS
{D}                     {return new Symbol(sym.ENTERO,yyline,yycolumn, yytext());} 
{DD}                    {return new Symbol(sym.DECIMAL,yyline,yycolumn, yytext());} 
{ID}                    {return new Symbol(sym.IDENTIFICADOR,yyline,yycolumn, yytext());}
{INICIACADENA}          {string.setLength(0); yybegin(STRING1);}
//FIN DATOS PRIMITIVOS 

\n                      {yycolumn=1;}

{BLANCOS}               {}

.                       {VentanaErrores.getVenErrores().AgregarError("Lexico","Token " + yytext() + " no reconocido", yyline , yycolumn);}

}

<STRING1>{
    {INICIACADENA}      {yybegin(YYINITIAL); return new Symbol(sym.CADENA,yyline,yycolumn,string.toString());}
    "\\n"               {string.append("\n");}
    "\\t"               {string.append("\t");}
    "\\r"               {string.append("\r");}
    "\\\\"              {string.append("\\");}
    "\\"                {string.append("\\\\");}
    "\\\""              {string.append("\"");}
    [^\"]               {string.append(yytext());}
}

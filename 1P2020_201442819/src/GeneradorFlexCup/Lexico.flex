/*
 * Desarrollado por Jerson Villatoro
 */
package GramaticaFlexCup;
import java_cup.runtime.Symbol;
import Editor.VentanaErrores; 

%% 
%class LexicoFS
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
 
BLANCOS =           [ \r\t]+
ID  =               [A-Za-z"_"]+["_"0-9A-Za-z]*
D   =               [0-9]+
DD  =               [0-9]+("."[0-9]+)
COMENTUNILINEA =    ("//".*\r\n)|("//".*\n)|("//".*\r)
COMENTMULTILINEA =  "/*""/"*([^*/]|[^*]"/"|"*"[^/])*"*"*"*/"
INICIACADENA =      "\""
INICIACADENA2 =      "'"

%state STRING1,STRING2

%%

<YYINITIAL>{

{COMENTUNILINEA}        {} 
{COMENTMULTILINEA}      {}  

"Nulo"                  {return new Symbol(sym.RNULO,yyline,yycolumn, yytext());}
"Var"                   {return new Symbol(sym.RVAR,yyline,yycolumn, yytext());}
"Imprimir"              {return new Symbol(sym.RIMPRIMIR,yyline,yycolumn, yytext());}
"Importar"              {return new Symbol(sym.RIMPORTAR,yyline,yycolumn, yytext());}
"Detener"               {return new Symbol(sym.RDETENER,yyline,yycolumn, yytext());}
"Selecciona"            {return new Symbol(sym.RSELECCIONA,yyline,yycolumn, yytext());}
"verdadero"             {return new Symbol(sym.RVERDADERO,yyline,yycolumn, yytext());}
"falso"                 {return new Symbol(sym.RFALSO,yyline,yycolumn, yytext());}
"Caso"                  {return new Symbol(sym.RCASO,yyline,yycolumn, yytext());}
"Retornar"              {return new Symbol(sym.RRETORNAR,yyline,yycolumn, yytext());}
"Si"                    {return new Symbol(sym.RSI,yyline,yycolumn, yytext());}
"Sino"                  {return new Symbol(sym.RSINO,yyline,yycolumn, yytext());}
"Defecto"               {return new Symbol(sym.RDEFECTO,yyline,yycolumn, yytext());}
"Funcion"               {return new Symbol(sym.RFUNCION,yyline,yycolumn, yytext());}
"Descendente"           {return new Symbol(sym.RDESCENDENTE,yyline,yycolumn, yytext());}
"Ascendente"            {return new Symbol(sym.RASCENDENTE,yyline,yycolumn, yytext());}
"Invertir"              {return new Symbol(sym.RINVERTIR,yyline,yycolumn, yytext());}
"Maximo"                {return new Symbol(sym.RMAXIMO,yyline,yycolumn, yytext());}
"Minimo"                {return new Symbol(sym.RMINIMO,yyline,yycolumn, yytext());}
"Filtrar"               {return new Symbol(sym.RFILTRAR,yyline,yycolumn, yytext());}
"Map"                   {return new Symbol(sym.RMAP,yyline,yycolumn, yytext());}
"Reduce"                {return new Symbol(sym.RREDUCE,yyline,yycolumn, yytext());}
"Todos"                 {return new Symbol(sym.RTODOS,yyline,yycolumn, yytext());}
"Alguno"                {return new Symbol(sym.RALGUNO,yyline,yycolumn, yytext());}
"LeerGxml"              {return new Symbol(sym.RLEERGXML,yyline,yycolumn, yytext());}
"ObtenerPorEtiqueta"    {return new Symbol(sym.ROBTENERPORETIQUETA,yyline,yycolumn, yytext());}
"ObtenerPorId"          {return new Symbol(sym.ROBTENERPORID,yyline,yycolumn, yytext());}
"ObtenerPorNombre"      {return new Symbol(sym.ROBTENERPORNOMBRE,yyline,yycolumn, yytext());}
"CrearVentana"          {return new Symbol(sym.RCREARVENTANA,yyline,yycolumn, yytext());}
"CrearContenedor"       {return new Symbol(sym.RCREARCONTENEDOR,yyline,yycolumn, yytext());}
"CrearTexto"            {return new Symbol(sym.RCREARTEXTO,yyline,yycolumn, yytext());}
"CrearCajaTexto"        {return new Symbol(sym.RCREARCAJATEXTO,yyline,yycolumn, yytext());}
"CrearAreaTexto"        {return new Symbol(sym.RCREARAREATEXTO,yyline,yycolumn, yytext());}
"CrearControlNumerico"  {return new Symbol(sym.RCREARCONTROLNUMERICO,yyline,yycolumn, yytext());}
"CrearDesplegable"      {return new Symbol(sym.RCREARDESPLEGABLE,yyline,yycolumn, yytext());}
"CrearBoton"            {return new Symbol(sym.RCREARBOTON,yyline,yycolumn, yytext());}
"CrearImagen"           {return new Symbol(sym.RCREARIMAGEN,yyline,yycolumn, yytext());}
"CrearReproductor"      {return new Symbol(sym.RCREARREPRODUCTOR,yyline,yycolumn, yytext());}
"CrearVideo"            {return new Symbol(sym.RCREARVIDEO,yyline,yycolumn, yytext());}
"Buscar"                {return new Symbol(sym.RBUSCAR,yyline,yycolumn, yytext());}
"AlClic"                {return new Symbol(sym.RALCLIC,yyline,yycolumn, yytext());}
"AlCargar"              {return new Symbol(sym.RALCARGAR,yyline,yycolumn, yytext());}
"AlCerrar"              {return new Symbol(sym.RALCERRAR,yyline,yycolumn, yytext());}
"CrearArrayDesdeArchivo"    {return new Symbol(sym.RCREARDESDEARCHIVO,yyline,yycolumn, yytext());}
"nulo"                      {return new Symbol(sym.RNULO,yyline,yycolumn, yytext());}
 


"="                     {return new Symbol(sym.IGUAL,yyline,yycolumn, yytext());}
"("                     {return new Symbol(sym.PARIZQ,yyline,yycolumn, yytext());} 
")"                     {return new Symbol(sym.PARDER,yyline,yycolumn, yytext());} 
"{"                     {return new Symbol(sym.LLAVEIZQ,yyline,yycolumn, yytext());} 
"}"                     {return new Symbol(sym.LLAVEDER,yyline,yycolumn, yytext());}
","                     {return new Symbol(sym.COMA,yyline,yycolumn, yytext());}
";"                     {return new Symbol(sym.PTCOMA,yyline,yycolumn, yytext());}
":"                     {return new Symbol(sym.DOSPUNTOS,yyline,yycolumn, yytext());}
"."                     {return new Symbol(sym.PUNTO,yyline,yycolumn, yytext());}
"["                     {return new Symbol(sym.CORIZQ,yyline,yycolumn, yytext());}
"]"                     {return new Symbol(sym.CORDER,yyline,yycolumn, yytext());}

//OPERADORES
"+"                     {return new Symbol(sym.MAS,yyline,yycolumn, yytext());}
"-"                     {return new Symbol(sym.MENOS,yyline,yycolumn, yytext());}
"*"                     {return new Symbol(sym.POR,yyline,yycolumn, yytext());}
"/"                     {return new Symbol(sym.DIV,yyline,yycolumn, yytext());}
"^"                     {return new Symbol(sym.POT,yyline,yycolumn, yytext());}
"<"                     {return new Symbol(sym.MENOR,yyline,yycolumn, yytext());}
">"                     {return new Symbol(sym.MAYOR,yyline,yycolumn, yytext());}
"<="                    {return new Symbol(sym.MENORIGUAL,yyline,yycolumn, yytext());}
">="                    {return new Symbol(sym.MAYORIGUAL,yyline,yycolumn, yytext());}
"=="                    {return new Symbol(sym.IGUALQUE,yyline,yycolumn, yytext());}
"!="                    {return new Symbol(sym.DIFERENTE,yyline,yycolumn, yytext());}
"&&"                    {return new Symbol(sym.AND,yyline,yycolumn, yytext());}
"||"                    {return new Symbol(sym.OR,yyline,yycolumn, yytext());}
"!"                     {return new Symbol(sym.NOT,yyline,yycolumn, yytext());}
"++"                    {return new Symbol(sym.AUMENTO,yyline,yycolumn, yytext());}
"--"                    {return new Symbol(sym.DECREMENTO,yyline,yycolumn, yytext());}
"*="                    {return new Symbol(sym.PORIGUAL,yyline,yycolumn, yytext());}
"+="                    {return new Symbol(sym.MASIGUAL,yyline,yycolumn, yytext());}
"-="                    {return new Symbol(sym.MENOSIGUAL,yyline,yycolumn, yytext());}
"/="                    {return new Symbol(sym.DIVIGUAL,yyline,yycolumn, yytext());}
"?"                     {return new Symbol(sym.TERNARIO,yyline,yycolumn, yytext());}
//FIN OPERADORES

//DATOS PRIMITIVOS
{D}                     {return new Symbol(sym.ENTERO,yyline,yycolumn, yytext());} 
{DD}                    {return new Symbol(sym.DECIMAL,yyline,yycolumn, yytext());} 
{ID}                    {return new Symbol(sym.IDENTIFICADOR,yyline,yycolumn, yytext());}
{INICIACADENA}          {string.setLength(0); yybegin(STRING1);} 
{INICIACADENA2}         {string.setLength(0); yybegin(STRING2);}
//FIN DATOS PRIMITIVOS 

\n                      {yycolumn=1;}

{BLANCOS}               {}

.                       {VentanaErrores.getVenErrores().AgregarError("Lexico","Token " + yytext() + " no reconocido", yyline , yycolumn);}

}

<STRING1>{
    {INICIACADENA}      {yybegin(YYINITIAL); return new Symbol(sym.CADENA,yyline,yycolumn,string.toString());}
    [^\"]               {string.append(yytext());}
}

<STRING2>{
    {INICIACADENA2}     {yybegin(YYINITIAL); return new Symbol(sym.CADENA,yyline,yycolumn,string.toString());}
    [^"'"]              {string.append(yytext());}
}

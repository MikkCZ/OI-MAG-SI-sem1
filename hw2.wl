(* ::Package:: *)

(* 
   Implementation of BOS state. 
   The state is represented by the list of pairs { varName, value }.
*)
initState[] :=
    {}; 

put[state_, varName_String, value_] :=
    Sow[
    Append[DeleteCases[state, {varName, _}], {varName, value}]
    ]; 

get[state_, varName_String] :=
    If[ MemberQ[state, {varName, _}],
        Last[First[Cases[state, {varName, _}]]],
        Null
    ]; 





(*Typing System*)
Clear[typeOf];
(*37*)typeOf[g_,n_Integer]:="int"
(*38*)typeOf[g_,d_Real]:="double"
(*39*)typeOf[g_,CBlock[{stm___}]]:=typeOf[g,{stm}];

(*40*)typeOf[g_,{}]:="command";
typeOf[g_,s_Symbol] /; get[g,ToString[s]]=!=Null :="command";

(*41*)typeOf[g_,{n_,stm___}] :=If[typeOf[g,n]=!=$Failed, typeOf[g,stm], $Failed];

(*42*)typeOf[g_,CAssign[var_,e_]] /; get[g,ToString[var]]=="double"&&typeOf[g,e]=="int":="command";
(*43*)typeOf[g_,CAssign[var_,e_]] /; get[g,ToString[var]]==typeOf[g,e]:="command";
(*44*)typeOf[g_,{a:CAssign[var_,e_],stm___}] /; typeOf[g,a]=!=$Failed :=typeOf[g,stm];
(*45*)typeOf[g_,CDeclare[type_,var_]] /; get[g,ToString[var]]==Null :="command"
(*46*)typeOf[g_,{CDeclare[type_,var_]}] := typeOf[g,CDeclare[type,var]];
(*46*)typeOf[g_,{CDeclare[type_,var_],stm___}] /; typeOf[g,CDeclare[type,var]]=="command" := typeOf[put[g,ToString[var],ToString[type]],stm]
typeOf[e_,f_]:=$Failed;

typeOf[{},-7]
typeOf[{},3.14]
typeOf[{},x]
typeOf[{},x]
typeOf[{},{}]
typeOf[{},CDeclare[int, x]]
typeOf[{},{CDeclare[int, x]}]
typeOf[{},{CDeclare[int, x], CDeclare[int, x]}]
typeOf[{},{CDeclare[int, x], CDeclare[double, x]}]
typeOf[{},{CDeclare[int, x], x}]
typeOf[{},{CDeclare[double, x], x}]
typeOf[{},{CDeclare[int, x], CAssign[x, 1]}]
typeOf[{},{CDeclare[int, x], CAssign[x, 1.2]}]
typeOf[{},{CDeclare[double, x], CAssign[x, 1]}]
typeOf[{},{CDeclare[double, x], CAssign[x, 1.2]}]
typeOf[{},CAssign[x, 42]]
typeOf[{},{CAssign[x, 42]}]

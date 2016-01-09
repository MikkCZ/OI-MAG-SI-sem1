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


(*Big-Step Operational Semantics*)
Clear[oneStep];
(*2*) oneStep[\[Sigma]_,CBlock[{s___}]]:=oneStep[\[Sigma],{s}];
oneStep[s_,e_]:={s,e}
oneStep[{},e_Symbol]:={s,Null}

(*3*) oneStep[\[Sigma]_,{}]:={\[Sigma],Null};

(*4*) oneStep[\[Sigma]_,{c_,p___}]:=oneStep[oneStep[\[Sigma],c][[1]],{p}];

(*5*) oneStep[\[Sigma]_,CDeclare[type_,var_]]:={put[\[Sigma],ToString[var],Undefined],Null};
(*6*) oneStep[s_,CWhile[e_,{stm___}]] /; oneStep[s,e][[2]]==0 := {oneStep[\[Sigma],{stm}][[1]],Null}
(*7*) oneStep[s_,CWhile[e_,{stm___}]] := {oneStep[oneStep[[oneStep[s,e][[1]],{stm}]][[1]],CWhile[e,{stm}]][[1]],Null}
(*8*) oneStep[s_,k_NumberQ]:={s,k}
(*9*) oneStep[s_,var_String]:={s,get[s,var]}
(*10*)oneStep[s_,CAssign[var_String,e_]]:=Module[ {tmp}, tmp=oneStep[s,e]; {put[tmp[[1]],var,tmp[[2]]] , tmp[[2]]} ]

oneStep[{},5]
oneStep[{},3.14]
oneStep[{},x]
oneStep[{{x, 10}},x]
oneStep[{},{}]
oneStep[{},CDeclare[int, x]]
oneStep[{},{CDeclare[int, x]}]
oneStep[{},{CDeclare[int, x], CDeclare[int, x]}]
oneStep[{},{CDeclare[int, x], CDeclare[double, x]}]
oneStep[{},{CDeclare[int, x], x}]
oneStep[{},{CDeclare[double, x], x}]




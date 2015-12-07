(* ::Package:: *)

(* 
   Implementation of denotational semantics state. 
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


(* Value rules *)
(*2*)DE[n_?NumberQ] := Function[{env},n];(* YOUR CODE HERE *);
(*3*)DE[true] := {}(* YOUR CODE HERE *);
(*4*)DE[false] := {}(* YOUR CODE HERE *);
(*5*)DE[v_String] := {}(* YOUR CODE HERE *);



(* Statement rules *)
(* YOUR CODE HERE *)


(* Expression rules *)
(* YOUR CODE HERE *)

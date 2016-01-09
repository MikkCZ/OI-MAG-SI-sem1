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

(*2*)oneStep[\[Sigma]_,CBlock[{s___}]]:={(* YOUR CODE HERE *)};

(*3*)oneStep[\[Sigma]_,{}]:={(* YOUR CODE HERE *)};

(*4*)oneStep[\[Sigma]_,{c_,p___}]:={(* YOUR CODE HERE *)};

(*5*)oneStep[\[Sigma]_,CDeclare[_,_]]:={(* YOUR CODE HERE *)};


(*Typing System*)

(*39*)typeOf[\[CapitalGamma]_,CBlock[{stm___}]]:={(* YOUR CODE HERE *)};

(*40*)typeOf[\[CapitalGamma]_,{}]:={(* YOUR CODE HERE *)};

(*41*)typeOf[\[CapitalGamma]_,{n_,stm___}]:={(* YOUR CODE HERE *)};

(*42*)typeOf[\[CapitalGamma]_,CAssign[var_,e_]]:={(* YOUR CODE HERE *)};

(*44*)typeOf[\[CapitalGamma]_,{a:CAssign[var_,e_],stm___}]:={(* YOUR CODE HERE *)};

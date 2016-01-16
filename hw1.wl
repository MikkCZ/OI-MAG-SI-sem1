(* ::Package:: *)

isRewritable[e_]:=(Or[Head[oneStepRewrite[e]]=!=oneStepRewrite,e==={}]);

notIntNotSym[s_Symbol]:=False;
notIntNotSym[n_Integer]:=False;
notIntNotSym[_]:=True;

(* Number *)
oneStepRewrite[derivative[n_Integer]]:={0};

(* Symbol - "x" *)
oneStepRewrite[derivative[x]]:={1};

(* Addition - "plus" *)
(* Your code here... *)
oneStepRewrite[plus[e_?PossibleZeroQ,f_]]:={f};
oneStepRewrite[plus[e_,f_?PossibleZeroQ]]:={e};
oneStepRewrite[plus[e_Integer,f_Integer]]:={Plus[e,f]};
oneStepRewrite[plus[e_,f_]] /; notIntNotSym[e]&&notIntNotSym[f] := {plus[oneStepRewrite[e][[1]],f],plus[e,oneStepRewrite[f][[1]]]};

(* Multiplication - "times" *)
(* Your code here... *)
oneStepRewrite[times[e_?PossibleZeroQ,f_]]:={0};
oneStepRewrite[times[e_,f_?PossibleZeroQ]]:={0};
oneStepRewrite[times[e_Integer,f_Integer]]:={Times[e,f]};
oneStepRewrite[times[x,f_]] /; notIntNotSym[f] := {times[x,oneStepRewrite[f][[1]]]};
oneStepRewrite[times[e_,x]] /; notIntNotSym[e] := {times[oneStepRewrite[e][[1]],x]};
oneStepRewrite[times[e_,f_]] /; notIntNotSym[e]&&notIntNotSym[f] := {times[oneStepRewrite[e][[1]],oneStepRewrite[f][[1]]]};
(*
oneStepRewrite[times[x, times[0, pow[x, -1]]]]*)

(* Power - "pow" *)
(* Your code here... *)
oneStepRewrite[pow[e_,0]]:={1};
oneStepRewrite[pow[0,n_Integer?Positive]]:={0};
oneStepRewrite[pow[e_?Positive,f_]]:={Power[e,f]};

(* Derivation - "derivative" *)
(* Your code here... *)
oneStepRewrite[derivative[plus[e_,f_]]]:={plus[derivative[e],derivative[f]]};
oneStepRewrite[derivative[times[e_,f_]]]:={plus[times[e,derivative[f]],times[derivative[e],f]]};
oneStepRewrite[derivative[pow[e_,n_]]]:={times[derivative[e],times[n,pow[e,n-1]]]};




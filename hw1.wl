(* ::Package:: *)

isRewritable[e_]:=(Or[Head[oneStepRewrite[e]]=!=oneStepRewrite,e==={}]);

(* Number *)
oneStepRewrite[derivative[n_Integer]]:={0};

(* Symbol - "x" *)
oneStepRewrite[derivative[x]]:={1};

(* Addition - "plus" *)
(* Your code here... *)

(* Multiplication - "times" *)
(* Your code here... *)

(* Power - "pow" *)
(* Your code here... *)

(* Derivation - "derivative" *)
(* Your code here... *)

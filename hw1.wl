(* ::Package:: *)

isRewritable[e_]:=(Or[Head[oneStepRewrite[e]]=!=oneStepRewrite,e==={}]);

(* Number *)
oneStepRewrite[derivative[n_Integer]]:={0};

(* Symbol - "x" *)
oneStepRewrite[derivative[x]]:={1};

(* Addition - "plus" *)
(* Your code here... *)
oneStepRewrite[plus[e_,f_]]:={Plus[e,f]};

(* Multiplication - "times" *)
(* Your code here... *)
oneStepRewrite[times[e_,f_]]:={Times[e,f]};

(* Power - "pow" *)
(* Your code here... *)
oneStepRewrite[pow[e_,f_]]:={Power[e,f]};

(* Derivation - "derivative" *)
(* Your code here... *)
oneStepRewrite[derivative[plus[e_,f_]]]:={plus[derivative[e],derivative[f]]};
oneStepRewrite[derivative[times[e_,f_]]]:={plus[times[e,derivative[f]],times[derivative[e],f]]};
oneStopRewrite[derivative[pow[e_,n_]]]:={times[derivative[e],times[n,pow[e,n-1]]]};

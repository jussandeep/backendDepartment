package com.example.DepartmentProj.restTempletConfigure;



import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

@Aspect
@Component
public class TransactionAspect {

	 private static final Logger logger = LoggerFactory.getLogger(TransactionAspect.class);
	    private final List<UndoOperation> undoOperations = new ArrayList<>();

	private final PlatformTransactionManager transactionManager;

	public TransactionAspect(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	// @Pointcut Pointcut to match all methods in service package
	//serviceMethods that targets all methods in the specified packages
	@Pointcut("execution(* com.example.DepartmentProj.depServices..*(..)), execution(* com.example.EmployeeStream.service..*(..)), execution(* com.example.DepartmentProj.depServices..*(..)) ")

	public void serviceMethods() {
		// Pointcut for service methods
	}

	@Around("serviceMethods()")
	public Object manageTransaction(org.aspectj.lang.ProceedingJoinPoint joinPoint) throws Throwable {
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionAttribute());
		try {
			Object result = joinPoint.proceed();
			transactionManager.commit(status);
			return result;
		} catch (Throwable ex) {
			transactionManager.rollback(status);
			throw ex;
		}
	}

	@Around("execution(* com.example.DepartmentProj.depServices..*(..)), execution(* com.example.EmployeeStream.service..*(..)), execution(* com.example.DepartmentProj.depServices..*(..)) ")
	public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();

		Object result = joinPoint.proceed();

		long elapsedTime = System.currentTimeMillis() - start;

		// Get method signature details
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getSignature().getDeclaringTypeName();
		String shortSignature = joinPoint.getSignature().toShortString();
		String longSignature = joinPoint.getSignature().toLongString();

		System.out.println(className + "." + methodName + " executed in Milli Seconds: " + elapsedTime + "ms");
		System.out.println("Short Signature: " + shortSignature);
		System.out.println("Long Signature: " + longSignature);

		return result;
	}
	
	

//	 @FunctionalInterface
//	 private interface UndoOperation {
//	  void undo();
//	 } 

	 // Adds an undo operation to the list
    public void addUndoOperation(UndoOperation undoOperation) {
        undoOperations.add(undoOperation);
    }

	    // Method to perform rollback
	    public void rollback() {
	        for (UndoOperation undoOperation : undoOperations) {
	            try {
	                undoOperation.undo();
	                logger.info("Undo operation executed: {}", undoOperation);
	            } catch (Exception e) {
	                logger.error("Error while rolling back: {}", e.getMessage());
	            }
	        }
	        // Clear the list after rollback
	        undoOperations.clear();
	    }

	    // Method to get the list of undo operations (optional)
//	    public List<UndoOperation> getUndoOperations() {
//	        return new ArrayList<>(undoOperations);
//	    }

	
	    // Clears the undo operations after a successful transaction
	    public void clearUndoOperations() {
	        undoOperations.clear();
	    }

    // Functional interface for undo operations
    @FunctionalInterface
    public interface UndoOperation {
        void undo();
    }
	
}


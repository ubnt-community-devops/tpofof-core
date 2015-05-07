package com.tpofof.core.security;

import com.tpofof.core.data.IPersistentModel;

public interface PermissionManager<PrincipalT, TargetT extends IPersistentModel<TargetT, TargetKeyT>, TargetKeyT, PermKeyT> {

	/**
	 * Throws exception if {@code principal} cannot perform {@code permissionKey} action on {@code target} asset.
	 * @param principal
	 * @param target
	 * @param permissionKey
	 */
	public void check(PrincipalT principal, TargetT target, PermKeyT permissionKey);
	
	/**
	 * Test if {@code principal} cannot perform {@code permissionKey} action on {@code target} asset.
	 * @param principal
	 * @param target
	 * @param permissionKey
	 * @param throwException if {@code false} return object is outcome of permission check, otherwise throws
	 * exception if check fails. 
	 * @return {@code true} if check passes, {@code false} if check fails.
	 */
	public boolean check(PrincipalT principal, TargetT target, PermKeyT permissionKey, boolean throwException);

	/**
	 * Throws exception if {@code principal} cannot perform {@code permissionKey} action on asset with {@code targetId}.
	 * @param principal
	 * @param target
	 * @param permissionKey
	 */
	public void check(PrincipalT principal, TargetKeyT targetKey, PermKeyT permissionKey);
	
	/**
	 * Test if {@code principal} cannot perform {@code permissionKey} action on asset with {@code targetId}..
	 * @param principal
	 * @param target
	 * @param permissionKey
	 * @param throwException if {@code false} return object is outcome of permission check, otherwise throws
	 * exception if check fails. 
	 * @return {@code true} if check passes, {@code false} if check fails.
	 */
	public boolean check(PrincipalT principal, TargetKeyT targetK, PermKeyT permissionKey, boolean throwException);
}

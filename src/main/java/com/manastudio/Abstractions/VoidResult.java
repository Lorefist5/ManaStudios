package com.manastudio.Abstractions;

/**
 * This class represents a result type designed specifically for scenarios where a `Result` type is used,
 * but no value needs to be returned (like deleting an object). The purpose of this class is to allow users to leverage the built-in
 * `Result` type with void-like behavior and still be able to tell if an operation was succesful. 
 * <p>
 * For example, when performing an operation that doesn't return a value, you can use:
 * <pre>
 *     Result<VoidResult> result = Result.success(new VoidResult());
 * </pre>
 * This helps maintain a consistent API for both value-returning and void-returning operations.
 * </p>
 * <p>
 * Key Features:
 * <ul>
 *   <li>Prevents inheritance to ensure immutability of its design and behavior.</li>
 *   <li>Acts as a placeholder for "void" return types in the `Result` system.</li>
 * </ul>
 * </p>
 */
public final class VoidResult {

    /**
     * Default constructor.
     * <p>
     * Since this class represents a placeholder for void operations, it does not contain any properties or methods.
     * </p>
     */
    public VoidResult() {
        // No properties to initialize; this is purely a placeholder.
    }

    /**
     * Factory method to create an instance of `VoidResult`.
     * <p>
     * While you can use the default constructor, this method provides better readability when constructing 
     * `Result` objects for void-like operations.
     * </p>
     * <pre>
     *     Result<VoidResult> result = Result.success(VoidResult.create());
     * </pre>
     *
     * @return a new instance of `VoidResult`.
     */
    public static VoidResult create() {
        return new VoidResult();
    }
}
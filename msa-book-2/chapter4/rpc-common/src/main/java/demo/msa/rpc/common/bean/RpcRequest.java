package demo.msa.rpc.common.bean;

/**
 * 封装 RPC 请求
 *
 * @author huangyong
 * @since 1.0.0
 */
public class RpcRequest {

  /**
   * 请求 ID
   */
  private String requestId;

  /**
   * 接口名称
   */
  private String interfaceName;

  /**
   * 方法名称
   */
  private String methodName;

  /**
   * 参数类型
   */
  private Class<?>[] parameterTypes;

  /**
   * 参数对象
   */
  private Object[] parameters;

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public String getInterfaceName() {
    return interfaceName;
  }

  public void setInterfaceName(String className) {
    this.interfaceName = className;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public Class<?>[] getParameterTypes() {
    return parameterTypes;
  }

  public void setParameterTypes(Class<?>[] parameterTypes) {
    this.parameterTypes = parameterTypes;
  }

  public Object[] getParameters() {
    return parameters;
  }

  public void setParameters(Object[] parameters) {
    this.parameters = parameters;
  }
}

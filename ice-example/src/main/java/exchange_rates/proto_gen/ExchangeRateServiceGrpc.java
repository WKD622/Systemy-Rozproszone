package proto_gen;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.20.0)",
    comments = "Source: exchangeint.proto")
public final class ExchangeRateServiceGrpc {

  private ExchangeRateServiceGrpc() {}

  public static final String SERVICE_NAME = "exchange_rates.ExchangeRateService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<proto_gen.ExchangeRateOpArguments,
      proto_gen.ExchangeRateOpResult> getGetExchangeRateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getExchangeRate",
      requestType = proto_gen.ExchangeRateOpArguments.class,
      responseType = proto_gen.ExchangeRateOpResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<proto_gen.ExchangeRateOpArguments,
      proto_gen.ExchangeRateOpResult> getGetExchangeRateMethod() {
    io.grpc.MethodDescriptor<proto_gen.ExchangeRateOpArguments, proto_gen.ExchangeRateOpResult> getGetExchangeRateMethod;
    if ((getGetExchangeRateMethod = ExchangeRateServiceGrpc.getGetExchangeRateMethod) == null) {
      synchronized (ExchangeRateServiceGrpc.class) {
        if ((getGetExchangeRateMethod = ExchangeRateServiceGrpc.getGetExchangeRateMethod) == null) {
          ExchangeRateServiceGrpc.getGetExchangeRateMethod = getGetExchangeRateMethod = 
              io.grpc.MethodDescriptor.<proto_gen.ExchangeRateOpArguments, proto_gen.ExchangeRateOpResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "exchange_rates.ExchangeRateService", "getExchangeRate"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto_gen.ExchangeRateOpArguments.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto_gen.ExchangeRateOpResult.getDefaultInstance()))
                  .setSchemaDescriptor(new ExchangeRateServiceMethodDescriptorSupplier("getExchangeRate"))
                  .build();
          }
        }
     }
     return getGetExchangeRateMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ExchangeRateServiceStub newStub(io.grpc.Channel channel) {
    return new ExchangeRateServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ExchangeRateServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ExchangeRateServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ExchangeRateServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ExchangeRateServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class ExchangeRateServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getExchangeRate(proto_gen.ExchangeRateOpArguments request,
        io.grpc.stub.StreamObserver<proto_gen.ExchangeRateOpResult> responseObserver) {
      asyncUnimplementedUnaryCall(getGetExchangeRateMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetExchangeRateMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                proto_gen.ExchangeRateOpArguments,
                proto_gen.ExchangeRateOpResult>(
                  this, METHODID_GET_EXCHANGE_RATE)))
          .build();
    }
  }

  /**
   */
  public static final class ExchangeRateServiceStub extends io.grpc.stub.AbstractStub<ExchangeRateServiceStub> {
    private ExchangeRateServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ExchangeRateServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ExchangeRateServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ExchangeRateServiceStub(channel, callOptions);
    }

    /**
     */
    public void getExchangeRate(proto_gen.ExchangeRateOpArguments request,
        io.grpc.stub.StreamObserver<proto_gen.ExchangeRateOpResult> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getGetExchangeRateMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ExchangeRateServiceBlockingStub extends io.grpc.stub.AbstractStub<ExchangeRateServiceBlockingStub> {
    private ExchangeRateServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ExchangeRateServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ExchangeRateServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ExchangeRateServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<proto_gen.ExchangeRateOpResult> getExchangeRate(
        proto_gen.ExchangeRateOpArguments request) {
      return blockingServerStreamingCall(
          getChannel(), getGetExchangeRateMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ExchangeRateServiceFutureStub extends io.grpc.stub.AbstractStub<ExchangeRateServiceFutureStub> {
    private ExchangeRateServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ExchangeRateServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ExchangeRateServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ExchangeRateServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_GET_EXCHANGE_RATE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ExchangeRateServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ExchangeRateServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_EXCHANGE_RATE:
          serviceImpl.getExchangeRate((proto_gen.ExchangeRateOpArguments) request,
              (io.grpc.stub.StreamObserver<proto_gen.ExchangeRateOpResult>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ExchangeRateServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ExchangeRateServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return proto_gen.ExchangeRateProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ExchangeRateService");
    }
  }

  private static final class ExchangeRateServiceFileDescriptorSupplier
      extends ExchangeRateServiceBaseDescriptorSupplier {
    ExchangeRateServiceFileDescriptorSupplier() {}
  }

  private static final class ExchangeRateServiceMethodDescriptorSupplier
      extends ExchangeRateServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ExchangeRateServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ExchangeRateServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ExchangeRateServiceFileDescriptorSupplier())
              .addMethod(getGetExchangeRateMethod())
              .build();
        }
      }
    }
    return result;
  }
}

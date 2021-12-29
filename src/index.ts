import mapper from "../template/mapper/index";
import service from "../template/service/index";
import serviceImpl from "../template/serviceImpl/index";
import controller from "../template/controller/index";
import unitTest from "../template/unitTest/index";

export default class CodeGenerator {
  constructor() {}
  static service = service;
  static mapper = mapper;
  static serviceImpl = serviceImpl;
  static controller = controller;
  static unitTest = unitTest;
}

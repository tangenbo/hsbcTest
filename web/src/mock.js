const Mock = require('mockjs');

const Random = Mock.Random;

/**
 * 购物车页面模拟数据
 * */
export let cart_list = function () {
    let goods = [];

    for(let i=0;i<5;i++){
        let newGoodsObj = {
            goods_id: Random.increment(),
            goods_name: Random.title(4),
            goods_price: Random.float(1,1000,2),
            num: Random.integer(1,100),
            goods_stock: Random.integer(100,9999),
            goods_img: Random.image('200x200','png'),
        };

        goods.push(newGoodsObj);
    }
    return goods;
};

Mock.mock('/api/cart_list','get',cart_list);

/**
 * 产品中心模拟产品数据
 * */
export let goods_list = function () {
    let goods = [];

    for(let i=0;i<12;i++){
        let newGoodsObj = {
            goods_id: Random.increment(),
            goods_name: Random.title(4),
            goods_price: Random.float(1,1000,2),
            num: Random.integer(1,100),
            stock_count: Random.integer(100,9999),
            img_arr: [
                Random.image('225x225'),
                Random.image('225x225','#000')
            ],
            goods_color_types: [
                Random.color(),
                Random.color(),
                Random.color()
            ],
            goods_specs: [
                Random.first(),
                Random.first(),
                Random.first()
            ],
            goods_tag: Random.word(),

        };
        goods.push(newGoodsObj);
    }
    return goods;

};
Mock.mock('/api/goods_list','get',goods_list);


/**
 * 根据产品ID和lang语言，获取产品详情接口
 * */
export let goods_detail = function () {
    let goods_detail = {
        goods_id: Random.increment(),
        goods_name: Random.title(4),
        goods_price: Random.float(1,1000,2),
        num: Random.integer(1,100),
        stock_count: Random.integer(100,9999),
        img_arr: [
            Random.image('225x225'),
            Random.image('225x225','#000')
        ],
        goods_color_types: [
            Random.color(),
            Random.color(),
            Random.color()
        ],
        goods_specs: [
            Random.first(),
            Random.first(),
            Random.first()
        ],
        goods_tag: Random.word(),

    };

    return goods_detail;


};
Mock.mock('/api/goods_detail','get',goods_detail);


/**
 * 获取所有的服务信息接口
 * */
export let service_list = function () {
    let services = [];

    for(let i=0;i<3;i++){
        let newService = {
            "id": Random.increment(),               // 服务ID
            "s_name": Random.name(),
            "s_intro": Random.sentence(),
            "s_price": Random.float(1,1000,2),       // 服务收费单价
            "s_img": Random.image('225x225')
        };
        services.push(newService);
    }
    return services;
};

Mock.mock('/api/getServiceList','get',service_list);

/**
 * 根据服务ID和语言代码，获取单个服务详情接口
 * */
export let service_detail = function() {
  let service_detail = {
      "id": Random.increment(),               // 服务ID
      "s_name": Random.name(),
      "s_intro": Random.sentence(),
      "s_price": Random.float(1,1000,2),       // 服务收费单价
      "s_img": Random.image('225x225')
  };

  return service_detail;
};
Mock.mock('/api/getServiveItem','get',service_detail);

/**
 * 获取所有的帖子信息接口
 * */
export let post_list = function() {
  let posts = [];

  for(let i=0;i<10;i++){
      let new_post = {
          "id":Random.increment(),                    // 帖子ID
          "view_count": Random.num(),
          "post_name": Random.name(),
          "post_intro": Random.sentence(),
          "post_img": Random.image('225x225'),
          "username": Random.first(),
          "user_role": Random.first(),
          "post_time": Random.date() // 发帖时间
      };
      posts.push(new_post);
  }

  return posts;

};
Mock.mock('/api/getPostList','get',post_list);

/**
 * 获取单个帖子详情接口
 * */
export let post_item = function() {
  let post_detail =  {
      "id":Random.increment(),                    // 帖子ID
      "view_count": Random.num(),
      "post_name": Random.name(),
      "post_intro": Random.sentence(),
      "post_img": Random.image('225x225'),
      "username": Random.first(),
      "user_role": Random.first(),
      "post_time": Random.date() // 发帖时间
  };

  return post_detail;
};
Mock.mock('/api/getPostItem','get',post_item);




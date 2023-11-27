package com.forumCommunity.service;


/*public class ZohoSubscriptionService {

    Logger logger = LoggerFactory.getLogger(ZohoSubscriptionService.class);

    @Autowired
    private ZohoRepository zohoRepository;


    @Value("${zoho.clientId}")
    private String clientId;
    @Value("${zoho.clientSecret}")
    private String clientSecret;
    @Value("${zoho.organizationId}")
    private String organizationId;
    @Value("${zoho.urls.accessAndRefreshToken}")
    private String accessAndRefreshTokenUrl;
    @Value("${zoho.urls.accessTokenFromRefreshToken}")
    private String accessTokenFromRefreshTokenUrl;
    @Value("${zoho.urls.revokeRefreshToken}")
    private String revokeRefreshTokenUrl;
    @Value("${zoho.redirectUrl}")
    private String redirectUrl;
    @Value("${zoho.subscriptions.api.default.ZOHO_API_BASE_URL}")
    private String  baseurl;

    @Autowired
    ZohoSessionDetails zohoSessionDetails;

    @Autowired
    ZohoSessionDetailsRepository zohoSessionDetailsRepository;

    @Autowired
    private  RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;


    @Autowired
    private ZohoPlanRepository zohoPlanRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private ZohoCouponRepository zohoCouponRepository;

    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ZohoPaymentRepository zohoPaymentRepository;
    @Autowired
    private
    CustomerRepository zohoCustomerRepository;
    @Autowired
    SubscriptionRepository zohoSubscriptionRepository;
    @Autowired
    private UserOrderRepository userOrderRepository;
    @Autowired
    private BasketRepository basketRepository;
    private HttpHeaders creteHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-com-zoho-subscriptions-organizationid", organizationId);
        headers.set("Authorization", "Zoho-oauthtoken " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private ZohoProduct buildZohoProduct(Product p) {
        ZohoProduct z =  new ZohoProduct();
        z.setName(p.getProductName());
        z.setDescription(p.getProductShortDescription());
        return z;
    }

    public Product createProduct(Product p) {
        ZohoProduct zp = this.createProduct(this.buildZohoProduct(p), this.zohoSessionDetails.getAccessToken());
        p.setZohoProductId(zp.getProductId());
        return p;
    }

    public ZohoProduct createProduct(ZohoProduct zohoProduct , String token){

        try{
            Map<String,Object >productObj = new HashMap<>();
            productObj.put("name",zohoProduct.getName());
            productObj.put("description",zohoProduct.getDescription());
            // productObj.put("email_ids",zohoProduct.getEmailId());
            // productObj.put("redirect_url",zohoProduct.getRedirectUrl());
            ResponseEntity<String> responseEntity = restTemplate.exchange(baseurl +"products", HttpMethod.POST, new HttpEntity<>(productObj,this.creteHeaders(token)), String.class);
            String responseBody = responseEntity.getBody();
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject productObj1 = jsonObject.getJSONObject("product");
            // ZohoProduct zohoProduct1  = new ZohoProduct();
            zohoProduct.setProductId(productObj1.getString("product_id"));
            // zohoProduct.setEmailId(productObj1.getString("email_ids"));
            // zohoProduct.setRedirectUrl(productObj1.getString("redirect_url"));
            zohoProduct.setName(productObj1.getString("name"));
            //zohoProduct.setDescription(productObj1.getString("description"));
            return  zohoRepository.save(zohoProduct);
        }catch (Exception ex){
            logger.error(("Failed to create product :")+ex.getMessage());
            throw  new MarketPlaceServiceException(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }


  *//*  private  ZohoProduct deleteZohoProduct(Product p){

         ZohoProduct zp = new ZohoProduct();
         zp.setProductId(p.getZohoProductId());
          return  zp;
    }*//*

    public  void deleteProduct(Product p){
        this.deleteProduct(p.getZohoProductId(), this.zohoSessionDetails.getAccessToken());
    }


    public  void deleteProduct(String productId , String token){
        try{
            //
            ResponseEntity<String> responseEntity = restTemplate.exchange(baseurl +"products/"+productId, HttpMethod.DELETE, new  HttpEntity<>(this.creteHeaders(token)), String.class);
            //    zohoRepository.deleteByProductId(productId);
            //   zohoRepository.deleteById(productId);
            //  zohoRepository.


        }catch (Exception ex){
            logger.error("failed to delete Product:"+ex.getMessage());
            throw  new MarketPlaceServiceException(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    private  ZohoProduct updateZohoProduct(Product p){
        ZohoProduct z=  new ZohoProduct();
        z.setName(p.getProductName());
        z.setDescription(p.getProductShortDescription());
        z.setProductId(p.getZohoProductId());
        return  z;

    }
    public Product updateProduct(Product p){
        ZohoProduct zp =this.updateProduct(this.updateZohoProduct(p),this.zohoSessionDetails.getAccessToken());
        p.setZohoProductId(zp.getProductId());
        return  p;
    }

    public  ZohoProduct updateProduct(ZohoProduct zohoProduct , String token ){

        // fetch the Product :-
        ResponseEntity<String> product=restTemplate.exchange(baseurl+"products/"+zohoProduct.getProductId(), HttpMethod.GET, new HttpEntity<>(this.creteHeaders(token)), String.class);
        String responseBody = product.getBody();

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject productObj1 = jsonObject.getJSONObject("product");

        Map<String,Object >productObj = new HashMap<>();
        productObj.put("product_id",zohoProduct.getProductId());
        productObj.put("name",zohoProduct.getName());
        productObj.put("description",zohoProduct.getDescription());
        // productObj.put("email_ids",zohoProduct.getEmailId());
        //   productObj.put("redirect_url",zohoProduct.getRedirectUrl());
        //update the Product
        ResponseEntity<String> responseEntity = restTemplate.exchange(baseurl+"products/"+zohoProduct.getProductId(), HttpMethod.PUT, new HttpEntity<>(productObj,this.creteHeaders(token)), String.class);
        return  zohoRepository.saveAndFlush(zohoProduct);

    }



    private  ZohoPlan getZohoPlan(Plan p,String zohoProductId){

        ZohoPlan zp = new ZohoPlan();
        zp.setProductId(zohoProductId);
        zp.setPlanCode(p.getPlanCode());
        zp.setName(p.getPlanName());
        zp.setRecurringPrice(p.getBlinkxPrice());
        zp.setPlanInterval(p.getPlanInterval());
        zp.setIntervalUnit(p.getPlanIntervalUnit());
        return zp;
    }

    public void createPlan(Product product){
        product.getPlans().stream().forEach(e->{
            ZohoPlan zp = this.createPlan(this.getZohoPlan(e,product.getZohoProductId()), this.zohoSessionDetails.getAccessToken());
            e.setZohoPlanId(zp.getPlanId());
        });
    }
    *//*public Set<Plan> createPlan(Set<Plan> plans) {
        plans.forEach(p->{
            ZohoPlan zp = this.createPlan(this.getZohoPlan(p), this.zohoSessionDetails.getAccessToken());
            p.setZohoPlanId(zp.getPlanId());
        });
        return plans;
    }*//*


    public  ZohoPlan createPlan(ZohoPlan zohoPlan , String token){
        try{
            Map<String, Object> planObj = new HashMap<>();
            planObj.put("plan_code",zohoPlan.getPlanCode());
            planObj.put("name",zohoPlan.getName());
            planObj.put("recurring_price",zohoPlan.getRecurringPrice());
            planObj.put("interval",zohoPlan.getPlanInterval());
            planObj.put("product_id",zohoPlan.getProductId());
            planObj.put("interval_unit", zohoPlan.getIntervalUnit());
            planObj.put("billing_cycles","-1");
            planObj.put("setup_price", zohoPlan.getSetupPrice());
            planObj.put("trial_period", zohoPlan.getTrialPeriod());
            ResponseEntity<String> responseEntity = restTemplate.exchange(baseurl +"plans", HttpMethod.POST, new HttpEntity<>(planObj,this.creteHeaders(token)), String.class);
            String responseBody = responseEntity.getBody();
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject planObj1 = jsonObject.getJSONObject("plan");
            zohoPlan.setPlanCode(planObj1.getString("plan_code"));
            zohoPlan.setName(planObj1.getString("name"));
            zohoPlan.setProductId(planObj1.getString("product_id"));
            zohoPlan.setRecurringPrice(planObj1.getDouble("recurring_price"));
            zohoPlan.setPlanInterval(planObj1.getInt("interval"));
            zohoPlan.setPlanId(planObj1.getString("plan_id"));
            return  zohoPlanRepository.save(zohoPlan);
        }catch (Exception ex){
            logger.error(("Failed to create plan :")+ex.getMessage());
            throw  new MarketPlaceServiceException(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }



    public  void deletePlan(String planCode ){
        try{
            //
            ResponseEntity<String> responseEntity = restTemplate.exchange(baseurl +"plans/"+planCode, HttpMethod.DELETE,  new  HttpEntity<>(this.creteHeaders(zohoSessionDetails.getAccessToken())), String.class);
            //  zohoPlanRepository.deleteByPlanCode(planCode);

        }catch (Exception ex){
            logger.error("failed to delete Plan:"+ex.getMessage());
            throw  new MarketPlaceServiceException(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    private  ZohoPlan updateZohoPlan(Plan p,String zohoProductId){
        //ResponseEntity<Plan> plan = restTemplate.exchange(baseurl+"plans/"+p.getPlanCode(),HttpMethod.GET,new HttpEntity<>(this.creteHeaders(this.zohoSessionDetails.getAccessToken())),Plan.class);


        ZohoPlan zp = new ZohoPlan();
        zp.setPlanCode(p.getPlanCode());
        zp.setProductId(zohoProductId);
        zp.setName(p.getPlanName());
        zp.setPlanId(p.getZohoPlanId());
        zp.setRecurringPrice(p.getBlinkxPrice());
        zp.setPlanInterval(p.getPlanInterval());
        zp.setIntervalUnit(p.getPlanIntervalUnit());
        return zp;
    }

    public void updatePlan(Product product){
        product.getPlans().stream().forEach(e->{
            ZohoPlan zp = this.updatePlan(this.updateZohoPlan(e,product.getZohoProductId()), this.zohoSessionDetails.getAccessToken());
            e.setZohoPlanId(zp.getPlanId());
        });
    }






    public ZohoPlan updatePlan(ZohoPlan zohoPlan , String token){
        try{
            ResponseEntity<String> plan=restTemplate.exchange(baseurl+"plans/"+zohoPlan.getPlanCode(), HttpMethod.GET, new HttpEntity<>(this.creteHeaders(token)), String.class);

            String responseBody = plan.getBody();


            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject exitsingPlanObj = jsonObject.getJSONObject("plan");
            Map<String,Object >planObj = new HashMap<>();
            planObj.put("plan_code",zohoPlan.getPlanCode());
            planObj.put("name",zohoPlan.getName());
            planObj.put("description",zohoPlan.getDescription());
            planObj.put("interval",zohoPlan.getPlanInterval());
            planObj.put("product_id",zohoPlan.getProductId());
            planObj.put("recurring_price",zohoPlan.getRecurringPrice());
            planObj.put("interval_unit", zohoPlan.getIntervalUnit());
            planObj.put("billing_cycles","-1");
            planObj.put("setup_price", zohoPlan.getSetupPrice());
            planObj.put("trial_period", zohoPlan.getTrialPeriod());
            ResponseEntity<String> responseEntity = restTemplate.exchange(baseurl+"plans/"+zohoPlan.getPlanCode(), HttpMethod.PUT, new HttpEntity<>(planObj,this.creteHeaders(token)), String.class);
            return  zohoPlanRepository.saveAndFlush(zohoPlan);

        }catch (Exception ex){
            logger.error("Failed to update plan"+ex.getMessage());
            throw new MarketPlaceServiceException(ex.getMessage() , HttpStatus.BAD_REQUEST);
        }
    }



    *//*
    Create Subscription
    required: Plan object , Zoho Customer Id

     1-find if zoho customer exists for this dummy email in our local records- if yes get zohocustomerid
     2-if customer doesnt exists - create customer -save in localdb and get customer id
     3- Create subscription vis zoho endpoint
     *//*


    public ZohoSubscription createSubscription(ZohoSubscription zohoSubscription, MyUserDetails ud) {
        *//*  if(zohoSubscription.isBasket()){

          }*//*

        ZohoCustomer customer = null;
//        String emailToVerify = ud.getPhoneNo() + "@blinkx.live";
        Optional<ZohoCustomer> isZohoCustomerAlreadyCreated = isCustomerExists(ud.getEmail());

        if (isZohoCustomerAlreadyCreated.isEmpty()) {
            customer = createCustomer(ud);
        } else
            customer = isZohoCustomerAlreadyCreated.get();

        try {
            Optional<Plan> plan = planRepository.findById(zohoSubscription.getPlanId());
            Map<String, Object> subscriptionObj = new HashMap<>();
            subscriptionObj.put("add_to_unbilled_charges", "false");
            subscriptionObj.put("auto_collect", false);
            subscriptionObj.put("customer", customer);
            subscriptionObj.put("coupon_code",zohoSubscription.getCouponCode());
            subscriptionObj.put("customer_id", customer.getCustomerId());
            List<Map<String,String>> customField = new ArrayList<>();
            //set the This Plan belongs to the basket or not
            Map<String,String> basketCustomField = new HashMap<>();
            basketCustomField.put("label","Basket");
            basketCustomField.put("value",zohoSubscription.isBasket()+"");
            customField.add(basketCustomField);
            //setting the Delivery_Email in subscription
            Map<String,String> deliveryEmail = new HashMap<>();
            deliveryEmail.put("label","Delivery_Email");
            deliveryEmail.put("value",zohoSubscription.getDeliveryEmail());
            logger.info(zohoSubscription.getDeliveryEmail());
            customField.add(deliveryEmail);
            subscriptionObj.put("custom_fields",customField);
            Map<String, String> plan1 = new HashMap<>();
            plan1.put("plan_code", plan.get().getPlanCode());
            plan1.put("billing_cycles","1");
            subscriptionObj.put("plan", plan1);
            ResponseEntity<String> responseEntity = restTemplate.exchange(baseurl + "subscriptions", HttpMethod.POST, new HttpEntity<>(subscriptionObj, this.creteHeaders(this.zohoSessionDetails.getAccessToken())), String.class);
            String responseBody = responseEntity.getBody();
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject zohoSubscriptionObj1 = jsonObject.getJSONObject("subscription");
            zohoSubscription.setSubscriptionId(zohoSubscriptionObj1.getString("subscription_id"));
            // zohoSubscription.setCouponCode(zohoSubscriptionObj1.getString("coupon_code"));
            zohoSubscription.setPlanId(plan.get().getPlanId());
            zohoSubscription.setCustomerEmail(customer.getEmail());
            zohoSubscription.setExpiresAt(AppUtil.getDate(zohoSubscriptionObj1.getString("expires_at"),"yyyy-MM-dd"));

            return subscriptionRepository.save(zohoSubscription);


        } catch (Exception ex) {
            logger.error(("Failed to create subscription") + ex.getMessage());
            throw new MarketPlaceServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    *//*
    1) Create customer in zoho and get customer id
    2) Save the customer in local db with zoho customer id received in step 1
     *//*
    public ZohoCustomer createCustomer(MyUserDetails ud) {
        try {
            ZohoCustomer zohoCustomer = new ZohoCustomer();
            Map<String, Object> customerObj = new HashMap<>();
            customerObj.put("display_name", ud.getFirstName());
            customerObj.put("first_name", ud.getFirstName());
            customerObj.put("last_name", ud.getLastName());
            customerObj.put("email", ud.getEmail());
            ResponseEntity<String> responseEntity = restTemplate.exchange(baseurl + "customers", HttpMethod.POST, new HttpEntity<>(customerObj, this.creteHeaders(this.zohoSessionDetails.getAccessToken())), String.class);
            String responseBody = responseEntity.getBody();
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject customerObj1 = jsonObject.getJSONObject("customer");
            zohoCustomer.setDisplayName(customerObj1.getString("display_name"));
            zohoCustomer.setEmail(customerObj1.getString("email"));
            zohoCustomer.setFirstName(customerObj1.getString("first_name"));
            zohoCustomer.setLastName(customerObj1.getString("last_name"));
            zohoCustomer.setCustomerId(customerObj1.getString("customer_id"));
            return customerRepository.save(zohoCustomer);
        } catch (Exception ex) {
            logger.error(("Failed to create customer") + ex.getMessage());
            throw new MarketPlaceServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    *//*
    Checks if user dummy email "phoneno@blinkx.live" exists in Zohocustomer lcoal table
    if exists , return customer else return null
     *//*
    public Optional<ZohoCustomer> isCustomerExists(String email) {
        try {
            Optional<ZohoCustomer> zohoCustomer = customerRepository.findByEmail(email);
            return zohoCustomer;
        } catch (Exception ex) {
            logger.error(("Failed to create customer") + ex.getMessage());
            throw new MarketPlaceServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    public Coupon createCoupon(Coupon coupon) {
        Map<String, Object> couponObj = new HashMap<>();
        couponObj.put("coupon_code", coupon.getCouponCode());
        couponObj.put("name", coupon.getName());
        couponObj.put("description", coupon.getDescription());
        couponObj.put("type", coupon.getType());
        //couponObj.put("duration",couponsMaster.getDuration());

        couponObj.put("discount_by", coupon.getDiscountBy());
        couponObj.put("discount_value", coupon.getDiscountValue());
        couponObj.put("product_id", coupon.getZohoProductId());
        couponObj.put("max_redemption", coupon.getMaxRedemption());
        couponObj.put("apply_to_plans", "select");
        List<Map<String, String>> plans = new ArrayList<>();
        coupon.getMappedPlans().forEach(m -> {
            Map<String, String> map = new HashMap<>();
            map.put("plan_code", m.getPlanCode());
            plans.add(map);
        });
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        if(coupon.getExpiryAt()!=null){
            couponObj.put("expiry_at", dateFormat.format(coupon.getExpiryAt()));
        }

        //couponObj.put("expiry_at",coupon.getExpiryAt());
        couponObj.put("plans", plans);
        ResponseEntity<String> res = restTemplate.exchange(baseurl + "coupons", HttpMethod.POST, new HttpEntity<>(couponObj, this.creteHeaders(this.zohoSessionDetails.getAccessToken())), String.class);
        coupon.setZohoCouponId(new JSONObject(res.getBody()).getJSONObject("coupon").optString("coupon_id"));
        return coupon;
    }

    public void deleteCoupon(String couponCode) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(baseurl + "coupons/" + couponCode, HttpMethod.DELETE, new HttpEntity<>(this.creteHeaders(this.zohoSessionDetails.getAccessToken())), String.class);
//            zohoCouponRepository.deleteByCouponCode(couponCode);
        } catch (Exception ex) {
            logger.error(("failed to delete the Coupon:") + ex.getMessage());
            throw new MarketPlaceServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public Coupon updateCoupon(Coupon couponsMaster) {
        try {
            ResponseEntity<String> coupon = restTemplate.exchange(baseurl + "coupons/" + couponsMaster.getCouponCode(), HttpMethod.GET, new HttpEntity<>(this.creteHeaders(this.zohoSessionDetails.getAccessToken())), String.class);
            String responseBody = coupon.getBody();
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject couponObj1 = jsonObject.getJSONObject("coupon");
            Map<String, Object> couponObj = new HashMap<>();
            couponObj.put("coupon_code", couponsMaster.getCouponCode());
            couponObj.put("name", couponsMaster.getName());
            couponObj.put("description", couponsMaster.getDescription());
            couponObj.put("type", couponsMaster.getType());
            // couponObj.put("duration",couponsMaster.getDuration());
            couponObj.put("discount_by", couponsMaster.getDiscountBy());
            couponObj.put("discount_value", couponsMaster.getDiscountValue());
            couponObj.put("max_redemption", couponsMaster.getMaxRedemption());
            couponObj.put("apply_to_plans", "select");
            couponObj.put("product_id", couponsMaster.getZohoProductId());
            List<Map<String, String>> plans = new ArrayList<>();
            couponsMaster.getMappedPlans().forEach(m -> {
                Map<String, String> map = new HashMap<>();
                map.put("plan_code", m.getPlanCode());
                plans.add(map);
            });
            DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
            if(couponsMaster.getExpiryAt()!=null){
                couponObj.put("expiry_at", dateFormat.format(couponsMaster.getExpiryAt()));
            }

            couponObj.put("plans", plans);
            ResponseEntity<String> responseEntity = restTemplate.exchange(baseurl + "coupons/" + couponsMaster.getCouponCode(), HttpMethod.PUT, new HttpEntity<>(couponObj, this.creteHeaders(this.zohoSessionDetails.getAccessToken())), String.class);
            // return  zohoCouponRepository.saveAndFlush(zohoCoupon);
            return couponsMaster;
        } catch (Exception ex) {
            logger.error(("failed to update the coupon:") + ex.getMessage());
            throw new MarketPlaceServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ZohoPayment createPayment(ZohoPayment zohoPayment) {
        try {
            Optional<ZohoCustomer> zohoCustomer = customerRepository.findByCustomerId(zohoPayment.getCustomerId());
            Map<String, Object> paymentObj = new HashMap<>();

            paymentObj.put("customer_id", zohoCustomer.get().getCustomerId());
            paymentObj.put("payment_mode", "banktransfer");
            paymentObj.put("amount", zohoPayment.getAmount());
            List<Map<String,String>> customField = new ArrayList<>();
//            zohoPayment.getZohoCustomFieldPayments().forEach(m->{
//                Map<String,String>map = new HashMap<>();
//                map.put("label",m.getLabel());
//                map.put("value",m.getValue());
//                customField.add(map);
//            });
            Map<String,String>map = new HashMap<>();
            map.put("label","RazorpayId");
            map.put("value",zohoPayment.getRazorpayLinkedPaymentAccount());
            customField.add(map);
            paymentObj.put("custom_fields", customField);
            ResponseEntity<String> res = restTemplate.exchange(baseurl + "payments", HttpMethod.POST, new HttpEntity<>(paymentObj, this.creteHeaders(this.zohoSessionDetails.getAccessToken())), String.class);
            zohoPayment.setZohoPaymentId(new JSONObject(res.getBody()).getJSONObject("payment").optString("payment_id"));
            logger.info(" zoho payment creation is done with zoho payment id "+zohoPayment.getZohoPaymentId() +" against the this customer id this " +zohoPayment.getCustomerId());
            return   zohoPaymentRepository.save(zohoPayment);
        } catch (Exception ex) {
            logger.error(("failed to  create the Payment") + ex.getMessage());
            throw new MarketPlaceServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    public Plan createPlanForExistingProductInZoho(Plan plan){
        try {
            Optional<Product>product=productRepository.findById(plan.getProductId());
            Map<String, Object> planObj = new HashMap<>();
            planObj.put("plan_code",plan.getPlanCode());
            planObj.put("name",plan.getPlanName());
            planObj.put("recurring_price",plan.getBlinkxPrice());
            planObj.put("interval",plan.getPlanInterval());
            planObj.put("product_id",product.get().getZohoProductId());
            planObj.put("interval_unit", plan.getPlanIntervalUnit());
            planObj.put("billing_cycles","-1");
            ResponseEntity<String> res = restTemplate.exchange(baseurl +"plans", HttpMethod.POST, new HttpEntity<>(planObj,this.creteHeaders(this.zohoSessionDetails.getAccessToken())), String.class);
            plan.setZohoPlanId(new JSONObject(res.getBody()).getJSONObject("plan").optString("plan_id"));
            return  plan;
        }catch (Exception ex){
            logger.error(("Failed to create Plan")+ex.getMessage());
            throw  new MarketPlaceServiceException(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }






    *//* To Generate Access Token and Refresh toke for first Time. *//*
    public ZohoSessionDetails setAccessTokenAndRefreshToken(String code) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("code", code);
            map.add("client_id", this.clientId);
            map.add("client_secret", this.clientSecret);
            map.add("redirect_uri", this.redirectUrl);
            map.add("grant_type", "authorization_code");
            ResponseEntity<String> response = restTemplate.exchange(this.accessAndRefreshTokenUrl, HttpMethod.POST, new HttpEntity<MultiValueMap<String, String>>(map, headers), String.class);
            JSONObject obj = new JSONObject(response.getBody());
            this.zohoSessionDetails.setAccessToken(obj.getString("access_token"));
            this.zohoSessionDetails.setRefreshToken(obj.getString("refresh_token"));
            zohoSessionDetails = this.zohoSessionDetailsRepository.save(zohoSessionDetails);
            return zohoSessionDetails;
        } catch (Exception e) {
            logger.error("setAccessTokenAndRefreshToken Failed " + e.getMessage());
            throw new MarketPlaceServiceException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    *//* To Refresh Access Token. *//*
    public String refreshAccessToken() {
        try {
            if (this.zohoSessionDetails.getRefreshToken() == null) {
                return "REFRESH_TOKEN_NULL";
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("refresh_token", this.zohoSessionDetails.getRefreshToken());
            map.add("client_id", this.clientId);
            map.add("client_secret", this.clientSecret);
            map.add("redirect_uri", this.redirectUrl);
            map.add("grant_type", "refresh_token");
            ResponseEntity<String> response = restTemplate.exchange(this.accessAndRefreshTokenUrl, HttpMethod.POST, new HttpEntity<MultiValueMap<String, String>>(map, headers), String.class);
            JSONObject obj = new JSONObject(response.getBody());
            this.zohoSessionDetails.setAccessToken(obj.getString("access_token"));
            this.zohoSessionDetails.setTimeStamp(new Date());
            this.zohoSessionDetailsRepository.save(zohoSessionDetails);
            return "SUCCESS";
        } catch (Exception e) {
            logger.error("refreshAccessToken Failed " + e.getMessage());
            throw new MarketPlaceServiceException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /// get Subscription By customer_id
    public List<SubscriptionsDetails> getZohoSubscriptions(String customerId) {
        try {
//            Optional<ZohoCustomer> zohoCustomer = customerRepository1.findByCustomerId(customerId);
//            if (zohoCustomer.isEmpty()){
//                throw new MarketPlaceServiceException(" Customer Id Is Wrong !",HttpStatus.BAD_REQUEST);
//            }
            ResponseEntity<String> response = restTemplate.exchange(baseurl+"subscriptions?customer_id="+customerId,HttpMethod.GET,new HttpEntity<>(this.creteHeaders(this.zohoSessionDetails.getAccessToken())),String.class);
            JSONObject obj = new JSONObject(response.getBody());
            JSONArray array=new JSONArray(obj.getJSONArray("subscriptions"));

            List<SubscriptionsDetails> subscriptionsDetails=new ArrayList<>();
            for (int i=0;i<array.length();i++){
                JSONObject subscription = array.getJSONObject(i);
                SubscriptionsDetails subscriptionsDetail=new SubscriptionsDetails(subscription);
                subscriptionsDetails.add(subscriptionsDetail);
            }
            return subscriptionsDetails;
        } catch (Exception ex) {
            logger.error(("failed to  get the subscription") + ex.getMessage());
            throw new MarketPlaceServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public List<SubscriptionsDetails> ZohoSubscriptions(String status, MyUserDetails myUserDetails) {
        try {
//            Optional<ZohoCustomer> zohoCustomer = customerRepository1.findByCustomerId(customerId);
//            if (zohoCustomer.isEmpty()){
//                throw new MarketPlaceServiceException(" Customer Id Is Wrong !",HttpStatus.BAD_REQUEST);
//            }
            Optional<ZohoCustomer> zohoCustomer = zohoCustomerRepository.findByEmail(myUserDetails.getEmail());
            if(zohoCustomer.isEmpty()) return new ArrayList<>();
            ResponseEntity<String> response = restTemplate.exchange(baseurl + "subscriptions?customer_id=" + zohoCustomer.get().getCustomerId() + "&filter_by=SubscriptionStatus." + status, HttpMethod.GET, new HttpEntity<>(this.creteHeaders(this.zohoSessionDetails.getAccessToken())), String.class);
            JSONObject obj = new JSONObject(response.getBody());
            JSONArray array = new JSONArray(obj.getJSONArray("subscriptions"));

            List<SubscriptionsDetails> subscriptionsDetails = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject subscription = array.getJSONObject(i);
                SubscriptionsDetails subscriptionsDetail = new SubscriptionsDetails(subscription);
                ZohoSubscription zohoSubscription = zohoSubscriptionRepository.findBySubscriptionId(subscriptionsDetail.getSubscriptionId());
                if(zohoSubscription==null)continue;
                if (zohoSubscription != null) {
                    UserOrder existingOrder = userOrderRepository.findByOrderId(zohoSubscription.getOrderId());
                    if (existingOrder.getBasket()) {
                        Basket basket = basketRepository.findById(existingOrder.getOrderedFor()).get();
                        subscriptionsDetail.setName(basket.getName());
                        subscriptionsDetail.setIsBasket(true);
                        subscriptionsDetail.setId(basket.getBasketId());
                    } else {
                        Product product = productRepository.findById(existingOrder.getOrderedFor()).get();
                        subscriptionsDetail.setName(product.getProductName());
                        subscriptionsDetail.setIsBasket(false);
                        subscriptionsDetail.setId(product.getProductId());
                    }
                } else logger.error(" can't find the subscription id in CMS ");
                subscriptionsDetails.add(subscriptionsDetail);
            }
            return subscriptionsDetails;
        } catch (Exception ex) {
            logger.error(("failed to  get the subscription ") + ex.getMessage());
            throw new MarketPlaceServiceException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public String setToken(ZohoSessionDetails zohoSessionDetails) {
        this.zohoSessionDetails.setAccessToken(zohoSessionDetails.getAccessToken());
        this.zohoSessionDetails.setRefreshToken(zohoSessionDetails.getRefreshToken());
        this.zohoSessionDetails.setTimeStamp(new Date());
        this.zohoSessionDetailsRepository.save(this.zohoSessionDetails);
        return "SUCCESS";
    }
}*/

# 0.1.6 Database Changes

## BidManagerView: Chain RFP View to group by hotel.city.name
```ecmascript 6
db.getCollection('BidManagerView').updateMany({type: "RFP", side: "SUPPLIER"}, {$set: {"group": "hotelRfpBid.hotel.city"}})
```

## Problem with validation rules in Questionnaire for BD4_END
Fix it by copying valid validation rules from rb-client master


## Problem with custom mandatory questions in questionnaires
Mandatory Questions in DB are marked with 1 and boolean is required instead

### RFP Collection Fix
```ecmascript 6
db.Rfp.find({"questionnaire.model.cells.cells.cells.req": {$not: {$type: "bool"}}}, {questionnaire: 1}).forEach( r  => {
    const q = r.questionnaire;
    const cleanedQ = cleanQuestionnaire(q);
    printjson(db.Rfp.updateOne({_id: r._id}, {$set: { questionnaire: cleanedQ }}))
    
    function cleanQuestionnaire(q){
        const model = q.model;
        model.cells.forEach( module => {
            module.cells && module.cells.forEach( section => {
                section.cells && section.cells.forEach( question => {
                    if(question.req !== undefined) {
                        question.req = !!(question.req)
                    }
                })
            })
        })
        return q;
    }
})
```

### Bid Collection Fix
```ecmascript 6
db.Bid.find({}, {questionnaire: 1, rfp: 1}).forEach( bid  => {
    const cleanedQ = cleanQuestionnaire(bid.questionnaire),
        rfpQuestionnaire = bid.rfp.questionnaire && cleanQuestionnaire(bid.rfp.questionnaire)
        update = {questionnaire: cleanedQ}
    if(rfpQuestionnaire) update["rfp.questionnaire"] = rfpQuestionnaire
    printjson(db.Bid.updateOne({_id: bid._id}, {$set: update}))
    
    function cleanQuestionnaire(q){
        const model = q.model;
        model.cells.forEach( module => {
            module.cells && module.cells.forEach( section => {
                section.cells && section.cells.forEach( question => {
                    if(question.req !== undefined) {
                        question.req = !!(question.req)
                    }
                })
            })
        })
        return q;
    }
})
```


## Chain Rep BM View
Bid Manager Views are now per Account instead of per UserAccount. This change affects BidManagerView and User Account 
collections.

##### Remove bmViews from UserAccount
```ecmascript 6
db.UserAccount.updateMany({}, {$unset: {bmViews: 1}})
```

##### Clean BidManagerViews Collection
Remove all BidManagerViews where Owner doesn't exist

```ecmascript 6
db.BidManagerView.find({}).forEach( bmView => {
    const uaCount = db.UserAccount.find({_id: bmView.ownerId}).count()
    if(uaCount === 0){
        db.BidManagerView.remove({_id: bmView._id})
    }
})
```

##### Create accountOwnerId
```ecmascript 6
db.BidManagerView.find({}).forEach( bmView => {
    db.UserAccount.find({_id: bmView.ownerId}, {accountId: 1}).forEach( ua => {
        printjson(db.BidManagerView.update({_id: bmView._id}, {$set: {accountOwnerId: ua.accountId}}))
    })
})
```

##### Create helper Collection
```ecmascript 6
db.BidManagerView.find({}).forEach( bmView => {
    const filter = { 
        accountOwnerId: bmView.accountOwnerId, 
        type: bmView.type,
        rfpType: bmView.rfpType,
        side: bmView.side
        }
    if(bmView.rfpId) filter.rfpId = bmView.rfpId
    const helperCursor = db.helperBmView.find(filter, {_id: 1})
    let groupTo = null;    

    if(helperCursor.hasNext()){
        const helperView = helperCursor.next()
        groupTo = helperView._id
    } else {
        db.helperBmView.insertOne(bmView)
        groupTo = bmView._id
    }
    
    printjson(db.BidManagerView.updateOne({_id: bmView._id}, { $set: {groupTo: groupTo}}))
    })
``` 

##### Create unique index on helper Collection
If this fails, something is wrong in previous command and process will fail

``` ecmascript 6
db.getCollection('helperBmView').createIndex({
    "ownerId" : 1,
    "type" : 1,
    "rfpType" : 1,
    "side" : 1,
    "rfpId" : 1
}, {unique: true})
```

##### Update UserAccount BM Views (! NOT IDEMPOTENT)
```ecmascript 6
db.UserAccount.find({}).forEach( ua => {
    const views = [ ua.defaultBmView ]
    if(ua.lastBmView && !ua.defaultBmView.equals(ua.lastBmView)) views.push(ua.lastBmView)
    const viewCursor = db.BidManagerView.find({ _id: { $in: views }}, {groupTo: 1})
    
    while(viewCursor.hasNext()){
        const v = viewCursor.next();
        if(ua.defaultBmView.equals(v._id)) { ua.defaultBmView = v.groupTo }
        if(ua.lastBmView && ua.lastBmView.equals(v._id)) { ua.lastBmView = v.groupTo }
    }
    
    const updates = {
        $set: { defaultBmView: ua.defaultBmView },
        $unset: { bmViews: 1 }
        }
    if(ua.lastBmView) { updates.$set.lastBmView = ua.lastBmView }
        
    printjson(db.UserAccount.updateOne({_id: ua._id}, updates))
    })
```

##### Cleanups

```ecmascript 6
db.BidManagerView.find({}).forEach(bmView => {
    if(bmView._id.equals(bmView.groupTo)){
        printjson(db.BidManagerView.updateOne({_id: bmView._id}, {$set: {ownerId: bmView.accountOwnerId}, $unset: {accountOwnerId: 1, groupTo: 1}}))
    } else {
        printjson(db.BidManagerView.remove({_id: bmView._id}))
    }
})
```

```ecmascript 6
db.helperBmView.drop()
```

```ecmascript 6
db.BidManagerView.createIndex({
    "ownerId" : 1,
    "type" : 1,
    "rfpType" : 1,
    "side" : 1,
    "rfpId" : 1
}, {unique: true})
```

## Supplier Contact Quick Sign Up
There was a bug when supplier contact was not updated in Bid on sign up. This MIGHT exist on live !!!
It is currently unknown how big impact this bug can have on site.  

for www
```ecmascript 6
db.getCollection('Bid').find({"supplier.contact": {$exists: true}, "supplier.contact.isUser": false}, {"supplier.contact": 1}).forEach( b => {
    const contactEmailAddress = b.supplier.contact.emailAddress, contactCompanyAccountId = b.supplier.contact.company.accountId
    db.User.find({emailAddress: contactEmailAddress}).forEach( user => {
        db.UserAccount.find({ userId: user._id, accountId: contactCompanyAccountId }).forEach( ua => {
            const newContact = {
                "id" : ua._id,
                "firstName" : user.firstName,
                "lastName" : user.lastName,
                "fullName" : user.firstName + ' ' + user.lastName,
                "emailAddress" : user.emailAddress.toLowerCase(),
                "jobTitle" : ua.jobTitle,
                "company": b.supplier.contact.company,
                "isUser" : true
            }
            if(user.phone) newContact.phone = user.phone
            if(user.profilePicture) newContact.profilePicture = user.profilePicture
            
            printjson( db.Bid.updateOne({ _id: b._id }, {$set: {"supplier.contact": newContact}}) )
        })
    })
})
```

## Adding NAM Cover Letter

RFP Templates are changed and need to be copied to deployments:
* Questionnaire for Basic Version is updated 
* Add chainSupport to ALL RFPs and Bids and set it to false
* Add hotelRfpType to ALL Bids and set it to DIRECT
* Create Letter Template collection

```ecmascript 6
db.getCollection('Rfp').updateMany({}, {$set: {"specifications.chainSupport": false}})
``` 

```ecmascript 6
db.getCollection('Bid').updateMany({}, {$set: {"rfp.specifications.chainSupport": false, "hotelRfpType": "DIRECT"  }})
```

## All Bid Supplier Contacts Email Addresses should be in lower case
```ecmascript 6
db.getCollection('Bid').find({"supplier.contact.emailAddress": {$exists: true}}).forEach( b =>{
    const emailAddress = b.supplier.contact.emailAddress.toLowerCase();
    db.Bid.updateOne({_id: b._id}, { $set: {"supplier.contact.emailAddress": emailAddress}})
    })
``` 

for www
```ecmascript 6
db.getCollection('Bid').find({"supplier.contact.emailAddress": {$exists: true}}).forEach( b =>{
    const emailAddress = b.supplier.contact.emailAddress.toLowerCase();
    printjson( db.Bid.updateOne({_id: b._id}, { $set: {"supplier.contact.emailAddress": emailAddress}}) )
})
``` 

## Bid Status Changed to Bid State

Bid Status is changed to Bid State to allow more complex states

```ecmascript 6
db.getCollection('Bid').find({}).forEach( b =>{
    const state = { status:  b.status.value, at: b.status.at, by: b.status.by._id || b.status.by };
    db.Bid.updateOne({_id: b._id}, { $set: {state: state}, $unset: { status: 1}})
    })
``` 

for www
```ecmascript 6
db.Bid.find({}, {status: 1}).forEach( b => {
    const status = b.status,
        state = { status:  status.value, at: status.at, by: status.by };
    printjson( db.Bid.updateOne({_id: b._id}, { $set: {state: state}, $unset: { status: 1}}) )
})
``` 


+ possible index updates

## Create QuestionnaireTemplate Collection OK

Questionnaire Templates are now loaded from Mongo Database. 

# TODO
## Send to NAM => Upload Responses => Hotel Default Answers should contain Sabre Code and all property codes should be without decimals

### All Hotels should have propcode cleaned and sabre code added to default answers

This script is not 100% safe. Try to create new ones
```ecmascript 6
db.getCollection('Hotel').find({}, {code: 1, sabreCode: 1}).forEach( h => {
    const update = { $set: { "answers.PROPCODE": parseInt(h.code)+"", "answers.SABRE_PROPCODE": h.sabreCode } }
    printjson( db.Hotel.updateOne({_id: h._id}, update) )
    printjson( db.RfpDefaultResponse.updateOne({_id: h._id}, update) )
})
``` 

#### On www code is already string
```ecmascript 6
db.getCollection('Hotel').find( {code: {$not: { $type: "string"}}} )
```
#### On www sabreCode is already string

#### On www answers.PROPCODE is already set

#### On www answers.SABRE_PROPCODE to be set

```ecmascript 6
db.Hotel.find( {"answers.SABRE_PROPCODE": {$exists: false}} ).forEach( h => {
    printjson(db.Hotel.updateOne({_id: h._id}, { $set: {"answers.SABRE_PROPCODE" : h.sabreCode }}))
})
```

#### On www RfpDefaultResponse
### All Bids should have propcode and sabre code in response and response draft
 

## Add User Profile Picture to Hotel Rfp Bid Supplier Contacts
```javascript 1.8
db.getCollection('User').find({profilePicture: {$exists: true}}).forEach(function (user){
    printjson( db.Bid.updateMany({"supplier.contact.id": { $in: user.userAccounts }}, {$set: { "supplier.contact.profilePicture": user.profilePicture }}) )
})
``` 

# 0.1.4 Database Changes

## Final Cleanup
```javascript 1.8
db.getCollection('Hotel').updateMany({}, { $unset: { googleLocations: 1, matched: 1}})

db.getCollection('Bid').updateMany({}, { $unset: { answers: 1, questionnaireOld: 1, offerOld: 1}})

db.getCollection('Rfp').updateMany({}, { $unset: {questionnaireOld: 1}})

```

## Update Hotel code and PROPCODE to String

```javascript 1.8
db.getCollection('Hotel').find({ code: { $not: { $type: 2}}}, {code: 1}).forEach( h => {
    var code = h.code+""
    db.Hotel.updateOne({_id: h._id}, { $set: {code: code, "answers.PROPCODE": code }})
    })
``` 

## FEATURE-RBS-229 Bid Manager - Revert LRA and NLRA average values to Season 1 RT 1

### Affected Collections:

1. Bid

```javascript 1.8
db.getCollection('Bid').find({offer: {$exists: true}}, {"status": 1, "offer": 1, "negotiations": 1}).forEach(function(bid){
    
    bid.offerOld = bid.offer
    delete bid.offer
    var lastNeg = getLastAgreedNegotiation()
    bid.offer = createNewOffer()
    
    print({
        lastNeg: lastNeg,
        negConfig: bid.negotiations.config,
        offer: bid.offer,
        offerOld: bid.offerOld
        })

    db.Bid.updateOne({_id: bid._id}, { $set: { offer: bid.offer, offerOld: bid.offerOld }})
    


    function getLastAgreedNegotiation(){
        var c = bid.negotiations.communication
        if(bid.status.value === 'NEGOTIATION_FINALIZED'){ return c[c.length-1].values}
        
        for(var i = c.length-1; i>=0; i--){
            var n = c[i]
            if(n.from.type === 'SUPPLIER') return n.values
        }
    }
        
    
    function createNewOffer(){
        var offer = {averages: bid.offerOld, main: {}}
        
        setRates(offer.main, lastNeg.rates)
        var primRate = getPrimaryRate(offer.main)
        setAmenities(offer.main, offer.averages, primRate)
        setTaxes(offer.main, offer.averages, primRate)
        setTcos(offer.main, primRate)
         
        return offer
    }


    function setRates(offer, rates){
       var lraSgl = (rates["lraS_s1_rt1"] || {}).value,
           lraDbl = (rates["lraD_s1_rt1"] || {}).value,
           nlraSgl = (rates["nlraS_s1_rt1"] || {}).value,
           nlraDbl = (rates["nlraD_s1_rt1"] || {}).value

        offer.lra = RateValue(lraSgl || lraDbl)
        offer.nlra = RateValue(nlraSgl || nlraDbl)
        
        var dyn = rates["dyn"]
        offer.dyn = dyn ? Dynamic(dyn.value) : Unavailable()
    }

    function RateValue(value){
        return value ? Fixed(value) : Unavailable()
    }
    
    function Fixed(amount){
        return {
            "type" : "FIXED",
            "amount" : Math.round(amount*100) / 100,
            "auxAmount" : 0,
            "isDerived" : false,
            "isIncluded" : false
        }
    }
    
    function Unavailable(){
        return {
            "type" : "UNAVAILABLE",
            "amount" : 0,
            "auxAmount" : 0,
            "isDerived" : false,
            "isIncluded" : false
        }
    }

    function Dynamic(amount){
        return {
            "type" : "PERCENTAGE",
            "amount" : amount,
            "auxAmount" : 0,
            "isDerived" : false,
            "isIncluded" : false
        }
    }

    function getPrimaryRate(offer){
        if(offer.lra && offer.lra.type === 'FIXED') return offer.lra
        if(offer.nlra && offer.nlra.type === 'FIXED') return offer.nlra
        return false
    }

    function setAmenities(main, averages, primRate){
        main.ec = earlyCheckout(primRate, averages.ec)
        main.prk = averages.prk
        main.bf = breakfast(primRate, averages.bf)
        main.ft = averages.ft
        main.ia = averages.ia
        main.wf = averages.wf
        main.as = averages.as
        
        main.amenitiesTotal = primRate ? total([main.ec, main.prk, main.bf, main.ft, main.ia, main.wf, main.as]) : Unavailable()
    }

    function earlyCheckout(primRate, average){
        var v = Object.assign({}, average)
        calculateAuxiliaryAmount(v, primRate)
        return v
    }

    function breakfast(primRate, average){
        var v = Object.assign({}, average)
        v.isDerived && calculateDerivedAmount(v, primRate)
        return v
    }

    function calculateAuxiliaryAmount(val, rate){
        if(val.type === 'PERCENTAGE' && rate && rate.type === 'FIXED'){
            val.auxAmount = Math.round(val.amount * rate.amount * 100) / 100
        }
    }
    
    function calculateDerivedAmount(val, rate) {
        if(val.isDerived && rate != null && rate.type === 'FIXED'){
            val.type = 'FIXED';
            val.amount = Math.round(val.auxAmount * rate.amount * 100 ) / 100
        }
    }

    function total(values){
        var sum = 0
        values.forEach(function (val) {
            if(val && val.type !== 'UNAVAILABLE' && !val.isIncluded) {
                sum += val.type === 'FIXED' ? val.amount : val.auxAmount
            }
        })
        
        return Fixed(sum)
    }


    function setTaxes(offer, avg, primRate){
        offer.lodging = calculateTax(avg.lodging, primRate);
        offer.state = calculateTax(avg.state, primRate);
        offer.city = calculateTax(avg.city, primRate);
        offer.vatGstRm = calculateTax(avg.vatGstRm, primRate);
        offer.vatGstFb = calculateTax(avg.vatGstFb, primRate);
        offer.service = calculateTax(avg.service, primRate);
        offer.occupancy = calculateTax(avg.occupancy, primRate);
        offer.other = calculateTax(avg.other, primRate);

        offer.taxesTotal = primRate ? total([offer.lodging, offer.state, offer.city, offer.vatGstRm, offer.vatGstFb, offer.service, offer.occupancy, offer.other]) : Unavailable()
    }

    function calculateTax(avg, rate){
        var v = Object.assign({}, avg)
        calculateAuxiliaryAmount(v, rate)
        return v
    }

    function setTcos(offer, primRate){
        offer.tcos = primRate ? total([primRate, offer.amenitiesTotal, offer.taxesTotal]) : Unavailable()
    }
})
```

## FEATURE-RBS-181 Update Hotel RFP Default Rate Grid (RfpTemplate updates skipped)
Add Second room type

### Affected Collections:
1. RfpTemplate

```javascript 1.8
// !!! REQUIRES MONGODB 3.6.x !!!
db.getCollection('RfpTemplate').update(
    {type: 'HOTEL'}, 
    {   
        $set: { 
            "questionnaire.response.state.$[elem].data.roomType": [1,2], 
            "questionnaire.config.$[elem].data.roomType": [1,2] 
        }
    }, 
    {
        multi: true, 
        arrayFilters: [ { "elem.data.roomType": {$exists: true}} ]
    }
)
```

## FEATURE-RBS-166 Not Updating entity on save
Handling entity changes. Needs update to ElasticSearch deployments for Chain and Hotel. Chain Code for brand and Master 
Chain needs to be added.

## FEATURE-RBS-138 Add row after sincerely (RfpTemplate updates skipped)
Update rfoTemplates collection so there is an extra line in the cover letter under Sincerely, ate the end of the letter.

1. RfpTemplate
```javascript 1.8
    db.RfpTemplate.updateMany({type: "HOTEL"},
        {
            $set: {
                "coverLetter": "<table style=\"width:100%\"><tbody><tr><td>{{buyer.contact.company.logo}}</td><td style=\"text-align:right\"><strong>RFP Due Date</strong><br />{{rfp.dueDate}}</td></tr></tbody></table><p style=\"text-align:justify\">{{supplier.contact.fullName}}<br />{{supplier.contact.company.name}}<br />{{supplier.contact.company.address.fullAddress}}</p><p style=\"text-align:right\">{{rfp.sentDate}}</p><p style=\"text-align:justify\">Dear {{supplier.contact.firstName}},</p><p style=\"text-align:justify\">{{buyer.contact.company.name}} is pleased to announce that you have been selected for inclusion in our {{rfp.programYear}} Hotel bidding and selection process.</p><p style=\"text-align:justify\">{{supplier.contact.firstName}}, because of the amount of travel spend {{buyer.company.name}} has in your hotel and city, we are inviting you to submit a rate and amenity package for our consideration. If accepted, your hotel will be one of a carefully chosen number of properties that will be included in our hotel program. Supporting {{buyer.company.name}} corporate travel policy, employee travelers will be utilizing an on-line booking tool that filters for only preferred vendors and contracts. An incorrect number will exclude your hotel from the preferred listing that employees will be selecting from for reservations.</p><p style=\"text-align:justify\">Your submission will be evaluated on the following criteria:</p><div><ul><li style=\"text-align:justify\">Rate should be a minimum of 15% below published corporate rate or at least 10% below the Consortia Rate.</li><li style=\"text-align:justify\">Rate should be competitive with those offered to other corporate accounts with similar spending potential</li><li style=\"text-align:justify\">Rate will be net, non-commissionable.</li><li style=\"text-align:justify\">Rate is run-of-the-house with last room availability (excluding suites, concierge, club and tower rooms).</li></ul></div><p style=\"text-align:justify\">There are three value-added amenities that will assist your bid greatly:</p><div style=\"text-align:justify\"><ul><li>Complimentary Breakfast (continental, full or buffet)</li><li>Waiver of telephone access fees for local, long-distance and toll free calls;</li><li>Waiver of wireless or broadband cable internet fees.</li></ul></div><p style=\"text-align:justify\">There are two other requirements that {{buyer.company.name}} has mandated.</p><div style=\"text-align:justify\"><ul><li>Selected hotels must agree to provide monthly room night volume reports. An electronic form will be sent at the end of each month for you to complete.</li><li>Selected hotel rates will be audited to assure compliance to contracted rates. Selected hotels must agree to pay the difference when disparities occur. <strong>As an example:</strong> If your hotels contracted rate for {{buyer.company.name}} is $145.00 and the audit shows a booking at $185.00 (when hotel had availability), hotel agrees to pay the difference.</li></ul></div><p style=\"text-align:justify\">To assist you in determining the {{rfp.programYear}} {{buyer.company.name}} Rate, be sure to consider the attached room night volumes by city during the period of {{rfp.programStartDate}} through {{rfp.programEndDate}}.</p><p style=\"text-align:justify\">Please complete the following hotel questionnaire and should you have any questions regarding our RFP, please contact (3rd party) at the phone number listed above.</p><p style=\"text-align:justify\">We are confident that {{rfp.programYear}} will be a most successful year for both our program and our valued hotel partners. We respectfully await your reply and look forward to the opportunity to work with your hotel in {{rfp.programYear}} .</p><p>Sincerely,</p><p>{{buyer.contact.fullName}}  <br />{{buyer.contact.jobTitle}} <br />{{buyer.contact.company.name}} <br />{{buyer.contact.email}} <br />{{buyer.contact.phone}} </p>",
                "finalAgreement" : "<table style=\"width:100%\"><tbody><tr><td>{{buyer.contact.company.logo}}</td><td style=\"text-align:right\">FINAL AGREEMENT SENT ON:<br />{{finalAgreement.date}}</td></tr></tbody></table>\n<p style=\"text-align:justify\">{{supplier.contact.fullName}}<br />{{supplier.contact.company.name}}<br />{{supplier.contact.company.address.fullAddress}}</p>\n<p style=\"text-align:justify\"><strong>Dear </strong>{{supplier.contact.firstName}}<strong>,</strong></p>\n<p>On behalf of {{buyer.company.name}}, I am pleased to inform you that your hotel has been accepted into our {{rfp.name}}.</p>\n<p>These rates, coded as “N” (negotiated), should be loaded under rate code A {{buyer.company.name}} for Sabre, Apollo/Galileo and Amadeus. All designated {{buyer.company.name}} Travel Agencies are attached for your use and review on the page below (Rate Loading Instructions)</p>\n<p>Per your submission, the {{rfp.programYear}} {{buyer.company.name}} rates and amenities will be published as follows:</p>\n<p>{{finalAgreement.rates}}</p>\n<p>In addition to the above rates the following amenities have been agreed to.</p>\n<p>{{finalAgreement.amenities}}</p>\n<p>The blackout dates are listed as follows:</p>\n<p>{{finalAgreement.blackoutDates}}</p>\n<p><strong>Rate Compliance Agreement</strong><br />Pursuant to our initial cover letter for {{rfp.name}} (dated {{rfp.sentDate}}), we anticipate running several rate compliance audits to assure your hotel provides our contracted rate (when availability allows). To that end, please find the necessary rate loading information below.</p>\n<p>{{finalAgreement.rateLoadingInformation}}</p><p>Sincerely,</p><p>{{buyer.contact.fullName}}<br />{{buyer.contact.jobTitle}}<br />{{buyer.contact.company.name}}<br />{{buyer.contact.email}}<br />{{buyer.contact.phone}}</p>"
            }
        }
    )
```

## FEATURE-RBS-58 Split Taxes
Questionnaire taxes group is split into multiple groups

### Affected Collections:
1. Rfp
1. Bid

```javascript 1.8
var groupsOrdinals = { 'HACP': 16, 'HACB': 17, 'HACFC': 18, 'HACLPC': 19, 'HACTFCCC': 20, 'HACWHSI': 21, 'HACWSHSI': 22, 'HACTTFA': 23, 'HACTTFLO': 24, 'CNRAT': 25, 'CIRRRFP': 26, 'CUD': 27 }
var newGroups = { "EC": 7, "LOT": 8, "STT": 9, "CIT":10, "VAT": 11, "VATFB": 12, "SET": 13, "OCT": 14, "OTT": 15 }
var questionsToGroups = { CANC_POL: 'EC', EARLYCK_FEE: 'EC', EARLYCK_UOM: 'EC', EARLYCK_INCLUDE: 'EC',
  LODGTX_FEE: 'LOT', LODGTX_UOM: 'LOT', LODGTX_INCLUDE: 'LOT',
  STATETX_FEE: 'STT', STATETX_UOM: 'STT', STATETX_INCLUDE: 'STT',
  CITYTX_FEE: 'CIT', CITYTX_UOM: 'CIT', CITYTX_INCLUDE: 'CIT',
  VATGSTRM_FEE: 'VAT', VATGSTRM_UOM: 'VAT', VATGSTRM_INCLUDE: 'VAT',
  VATGSTFB_FEE: 'VATFB', VATGSTFB_UOM: 'VATFB', VATGSTFB_INCLUDE: 'VATFB',
  SERVICE_FEE: 'SET', SERVICE_UOM: 'SET', SERVICE_INCLUDE: 'SET',
  OCC_FEE: 'OCT', OCC_UOM: 'OCT', OCC_INCLUDE: 'OCT',
  OTHERTX_FEE: 'OTT', OTHERTX_FEE_UOM: 'OTT', OTHERTX_FEE_DESC: 'OTT', OTHERTX_FEE_INCL: 'OTT' }

function handleGroup(group){
  if(group) {
    var newGroupOrdinal = groupsOrdinals[group.id]
    if(newGroupOrdinal) { group.ord = newGroupOrdinal }
  }  
}

function handleTaxes(groups, index, group){
  group && group.cells && group.cells.forEach( function (q) {
    var qGroupId = questionsToGroups[q.id]
    var group = groups.find(function (tg) { return tg.id === qGroupId })
    if(!group){
      group = {
        id: qGroupId,
        ord: newGroups[qGroupId],
        cells: []
      }
      groups.push(group)
    }
    group.cells.push(q)
  })

  groups.splice(index, 1)
  handleGroup(groups[index]) // because of splice
}

function updateQuestionnaire(questionnaire){
  var clientSpecificModule = questionnaire && questionnaire.model.cells[1]
  clientSpecificModule = clientSpecificModule && clientSpecificModule.id === 'CS' ? clientSpecificModule : undefined

  if(clientSpecificModule && clientSpecificModule.cells){
    clientSpecificModule.cells.forEach( function(group, index, cells) {
      if(group.id === 'FT') {
        handleTaxes(cells, index, group)
      } else {
        handleGroup(group)
      }
    })

    clientSpecificModule.cells.sort(function (a,b) { return a.ord - b.ord })
  }
}

db.getCollection('RfpTemplate').find({}).forEach(function(rt){
  updateQuestionnaire(rt.questionnaire)
  db.getCollection('RfpTemplate').updateOne({_id: rt._id}, { $set: { questionnaire: rt.questionnaire }})
  print({ _id: rt._id, status: 'OK' })
})

db.getCollection('Rfp').find({}).forEach(function(rfp){
  updateQuestionnaire(rfp.questionnaire)
  db.getCollection('Rfp').updateOne({_id: rfp._id}, { $set: { questionnaire: rfp.questionnaire }})
  print({ _id: rfp._id, status: 'OK' })
})

db.getCollection('Bid').find({}).forEach(function(bid){
  updateQuestionnaire(bid.rfp.questionnaire)
  updateQuestionnaire(bid.questionnaire)
  db.getCollection('Bid').updateOne({_id: bid._id}, { $set: { "rfp.questionnaire": bid.rfp.questionnaire, "questionnaire": bid.questionnaire }})
  print({ _id: bid._id, status: 'OK' })
})

```

## FEATURE-RBS-117 Join User Data
All Created.by and Status.by to user id only. Added indexes for buyer and supplier contact id in RFP and BID.

```javascript 1.8
function rbs117JoinUserDataDbUpdate(){

	updateCollection("Account");

	updateCollection("Bid");
	db.getCollection("Bid").createIndex({"buyer.contact.id" : 1}, { "name": "buyerContactUserAccountId"})
	db.getCollection("Bid").createIndex({"supplier.contact.id" : 1}, { "name": "supplierContactUserAccountId"})

	updateCollection("Chain");
	updateCollection("Company");
	updateCollection("Hotel");
	updateCollection("HotelManagementCompany");
	
	updateCollection("Invitation");

	updateCollection("Rfp");
	db.getCollection("Rfp").createIndex({"specifications.buyer.contact.id" : 1}, { "name": "buyerContactUserAccountId"})
	
	updateCollection("TravelAgency");
	updateCollection("TravelConsultancy");

	updateCollection("TravelDestination");

	updateCollection("UnvalidatedChain");
	updateCollection("UnvalidatedCompany");
	updateCollection("UnvalidatedHotel");
	updateCollection("UnvalidatedHotelManagementCompany");
	updateCollection("UnvalidatedTravelAgency");
	updateCollection("UnvalidatedTravelConsultancy");
	
	function updateCollection(collectionName){
		db.getCollection(collectionName).updateMany({"created.by._id": {$exists: true} }, { $rename: {"created.by._id": "created.byId", "status.by._id": "status.byId" }})
		db.getCollection(collectionName).updateMany({"created.byId" : {$exists: true}}, { $rename: {"created.byId": "created.by", "status.byId": "status.by" }})
	}
}
```

## FEATURE-RBS-7 Update TD Office Filter
Update TD Office Filter distance Value in DB for unchanged filters

### Affected Collections:

1. TravelDestination

```javascript 1.8
db.getCollection('TravelDestination').updateMany(
    { type: 'OFFICE', 'filter.maxDistance.value': 3.0 },
    { $set: { 'filter.maxDistance.value': 5.0 } }
)
```

## Feature Bid Manager High Speed Internet Access
### Affected Collections:

1. BidManagerView

#### Database Patch

##### BidManagerView Collection
```javascript
db.BidManagerView.find({}).forEach( function (bmView) {
    var iaIndex = bmView.columns.indexOf('hotelRfpBid.ia');
    if(iaIndex === -1) {
        var fitnessIndex = bmView.columns.indexOf('hotelRfpBid.fitness');
    
        if(fitnessIndex !== -1 ){
            bmView.columns.splice(fitnessIndex, 0, 'hotelRfpBid.ia')
        } else {
            printjson({'error': bmView._id})
        }
  
        db.BidManagerView.replaceOne( {_id: bmView._id}, bmView);
    }
})
```

## Feature Add Master Chain to Bid Manager

#### Database Patches

##### BidManagerView Collection
```javascript
db.getCollection('BidManagerView').find({side: "BUYER"}).forEach(function(bmv){
    var updated = false;
    
    if(bmv.type === 'RFP') {
        if( bmv.columns[5] !== 'hotelRfpBid.hotel.chainName' ) { bmv.columns.splice(5, 0, 'hotelRfpBid.hotel.chainName'); updated = true }
        if( bmv.columns[6] !== 'hotelRfpBid.hotel.brandName' ) { bmv.columns.splice(6, 0, 'hotelRfpBid.hotel.brandName'); updated = true }
    } else if(bmv.type === 'ALL') {
        if( bmv.columns[6] !== 'hotelRfpBid.hotel.chainName' ) { bmv.columns.splice(6, 0, 'hotelRfpBid.hotel.chainName'); updated = true }
        if( bmv.columns[7] !== 'hotelRfpBid.hotel.brandName' ) { bmv.columns.splice(7, 0, 'hotelRfpBid.hotel.brandName'); updated = true }
    }

    if(updated){ db.BidManagerView.updateOne( {_id: bmv._id}, { $set: { columns: bmv.columns } } ) }

})
```  

## Master Chain Codes
### Affected Collections:

1. Chain
1. Hotel
1. Bid
1. TravelDestination

##### Chain
```javascript 1.8

/**************************************************************
  CREATE NEW CHAIN COLLECTION TO WITH NEW SCHEMA
**************************************************************/

db.Chain.find({}).forEach(function(c){

  c.code = c._id;
  delete c._id;
  
  if(c.master){
    c.marketer = c.master
    delete c.master
  }

  c.subtype = 'BRAND'

  db.ChainNew.insert(c);
})

/**************************************************************
  ADD MASTER CHAINS
**************************************************************/

var masterChains = [
  { "name": "Hyatt", "code": "HY", "brands": ["HY", "HU"] },
  { "name": "Marriott", "code": "EM", "brands": ["AR", "AL", "AK", "BG", "CY", "DE", "EB", "EL", "FN", "GE", "GX", "MD", "LC", "ET", "MC", "VC", "ZX", "OX", "PR", "BR", "RC", "RZ", "SI", "XV", "XR", "TO", "TX", "WH", "WI" ] },
  { "name": "IHG", "code": "6C", "brands": ["AN", "MA", "YO", "CP", "VN", "HI", "IN", "UL", "IC", "SP", "YZ"] },
  { "name": "Wyndham", "code": "WY", "brands": ["BU", "DI", "BH", "HJ", "KG", "MT", "RA", "OZ", "TL", "WT", "WG", "WY"] },
  { "name": "Hilton", "code": "EH", "brands": ["PY", "CN", "QQ", "DT", "ES", "HX", "GI", "HH", "HL", "HT", "HG", "WA"] },
  { "name": "Choice", "code": "EC", "brands": ["NZ", "EZ", "CC", "CI", "CZ", "EO", "MZ", "QI", "RI", "SZ", "UB"] },
  { "name": "Accor", "code": "RT", "brands": ["RT", "PU", "SB"] },
  { "name": "Best Western", "code": "BW", "brands": ["BW"] },
  { "name": "Carlson Rezidor", "code": "CW", "brands": ["CX", "PD", "PK", "QC", "RD"] }
];

masterChains.forEach(function(mc){
  db.ChainNew.insert({
      "name" : mc.name,
      "code" : mc.code,
      "status" : {
          "at" : ISODate("2000-01-01T00:00:00.000Z"),
          "by" : {
              "_id" : ObjectId("000000000000000000000000"),
              "firstName" : "PRELOADED"
              },
          "value" : "ACTIVE"
      },
      "created" : {
        "at" : ISODate("2000-01-01T00:00:00.000Z"),
        "by" : {
            "_id" : ObjectId("000000000000000000000000"),
            "firstName" : "PRELOADED"
        }
      },
      "industry" : "TRAVEL_AND_HOSPITALITY",
      "type" : "CHAIN",
      "subtype" : "MASTER"
  })
})


/**************************************************************
  SET MASTER CHAINS TO CHAINS
**************************************************************/

db.ChainNew.find({subtype: "MASTER"}).forEach(function (mc){
  var brands = masterChains.find(function(c) { return c.code === mc.code}).brands;

  db.ChainNew.updateMany({subtype: "BRAND", code: { $in: brands }}, { $set: { master: mc}})
  
  print({code: mc.code, brands: brands})
})

/**************************************************************
  REPLACE OLD COLLECTION WITH NEW
**************************************************************/

db.Chain.drop();
db.ChainNew.renameCollection("Chain");
```

##### Hotel Collection
```javascript 1.8
/**************************************************************
  EXTRACT EAN CHAIN DATA
**************************************************************/
db.Hotel.updateMany({}, { $rename: { "chain.ean": "chainEan"} })

/**************************************************************
  UPDATE ALL HOTELS WITH NEW CHAINS
**************************************************************/
db.Chain.find({subtype: "BRAND"}).forEach(function(c){
  db.Hotel.updateMany({"chain._id": c.code}, { $set: { "chain": c} })
})
```

##### Bid Collection
```javascript 1.8
/**************************************************************
  UPDATE ALL HOTEL RFP BIDS WITH NEW CHAINS
**************************************************************/

var counter = 0

db.Bid.find({"supplier.company.type": "HOTEL"}, {"supplier": 1}).forEach(function(b){

  db.Hotel.find({_id: b.supplier.company.entityId}, {chain: 1}).forEach(function(h){
    if(b.supplier.contact && b.supplier.contact.company && b.supplier.contact.company.entityId === h._id){
      db.Bid.updateOne({_id: b._id}, { $set: { "supplier.company.chain": h.chain, "supplier.contact.company.chain": h.chain }})
    } else {
      db.Bid.updateOne({_id: b._id}, { $set: { "supplier.company.chain": h.chain }})
    }
  })
  counter++;
})

print(counter)
```

#### TravelDestination
TravelDestination.filter.chains should be updated from chain code to chain _id (must be a String not ObjectId because of 
NoChain filter value).

**Currently there is no save chains filter for any TravelDestination so no update code is provided.** If it appears in the 
meantime, it might be easier to update it manually

## FEATURE-RBE-123 ReadyBid Property Code (already implemented)
Replace Sabre Property Code with ReadyBid one

### Affected Collections:

1. Hotel
1. Bid
1. RfpDefaultAnswers
1. RfpDefaultResponse

#### Hotel

**Create integer version of sabre code**
```javascript 1.8
db.getCollection('Hotel').find({}).forEach(function(h){ 
    db.Hotel.updateOne({_id: h._id}, {$set: { sCodeN: parseInt(h.sabreCode)}}) 
})
```

**Create temporary index on sCodeN field**
```javascript 1.8
db.getCollection('Hotel').createIndex(
   { sCodeN: 1 },
   { name: "sCodeN_1", unique: true }
)
```

**Create ReadyBid code for all hotels**

```javascript 1.8
var id = 1;

db.Hotel.find({}).sort({sCodeN: 1}).forEach(function(h) {
    var code = id*10+generateCheckDigit(id);
    db.Hotel.updateOne({_id: h._id}, { $set: { code: code, "answers.PROPCODE": code }});
    id++;
})

function generateCheckDigit( n ){
    
  var n1 = parseInt(n), sum = 0, odd = true;
  
  while(n1 > 0){
    var d = n1 / 10;
    n1 = Math.floor(d);
    sum += Math.round(odd ? 30 * (d-n1) : 10 * (d-n1));
    odd = !odd
  }
  
  var m = sum % 10;
  return m === 0 ? 0 : 10 - m;
}
```

**Code creation check**
```javascript 1.8
db.Hotel.find({code: {$exists: false}})
```

**Drop temporary index**
```javascript 1.8
db.getCollection('Hotel').dropIndex( "sCodeN_1" )
```

**Create unique index for ReadyBid Property Code**
```javascript 1.8
db.getCollection('Hotel').createIndex(
   { code: 1 },
   { name: "code_1", unique: true }
)
```

**Remove temporary sabre code field**
```javascript 1.8
db.getCollection('Hotel').updateMany({}, {$unset: {sCodeN: 1}})
```

#### Bid (old Questionnaire version)

**Replace PROPCODE with ReadyBid Code**
```javascript 1.8
var count = 0;
db.Bid.find({}).forEach(function(b){
    db.Hotel.find({_id: b.supplier.company.entityId}).forEach(function(h){
        var update = {"answers.PROPCODE": h.code+""};
        
        if(b.answersDraft) {
            update["answersDraft.PROPCODE"] = h.code+""
        }
        db.Bid.updateOne({_id: b._id}, {$set: update})
        count++
    })
})
printjson({updated: count})
```


#### Bid (new Questionnaire version)

**Replace PROPCODE with ReadyBid Code**
```javascript 1.8
var count = 0;
db.Bid.find({"questionnaire.response": {$exists: true}}).forEach(function(b){
    db.Hotel.find({_id: b.supplier.company.entityId}).forEach(function(h){
        var update = {"questionnaire.response.answers.PROPCODE": h.code+""};
        if(b.questionnaire.responseDraft) {
            update["questionnaire.responseDraft.answers.PROPCODE"] = h.code+""
        }
        db.Bid.updateOne({_id: b._id}, {$set: update})
//         update.PROPCODE = b.questionnaire.response.answers.PROPCODE;
//         printjson(update)
        count++
    })
})
printjson({updated: count})
```

#### RfpDefaultAnswers (old Questionnaire version only)
```javascript 1.8
var count = 0;
db.RfpDefaultAnswers.find({}).forEach(function(rda){
    db.Hotel.find({_id: rda._id}).forEach(function(h){
        var update = {"answers.PROPCODE": h.code+""};
        db.RfpDefaultAnswers.updateOne({_id: rda._id}, {$set: update})
        count++
    })
})
printjson({updated: count})
```

#### RfpDefaultResponse (new Questionnaire version only)
```javascript 1.8
var count = 0;
db.RfpDefaultResponse.find({}).forEach(function(rda){
    db.Hotel.find({_id: rda._id}).forEach(function(h){
        var update = {"answers.PROPCODE": h.code+""};
        db.RfpDefaultResponse.updateOne({_id: rda._id}, {$set: update})
        count++
    })
})
printjson({updated: count})
```

## Feature Bid Response Handling
Bid Manager to show averages

### Affected Collections:

1. Bid

#### Database Patches

##### Bid Collection

```javascript
db.getCollection('Bid').find({"negotiations.communication": {$exists: true}}).forEach( function (bid){
    
    bid.offerOld = bid.offer
    delete bid.offer
    var lastNeg = getLastAgreedNegotiation()
    bid.offer = createNewOffer()
    
//     print({
//         lastNeg: lastNeg,
//         negConfig: bid.negotiations.config,
//         offer: bid.offer,
//         offerOld: bid.offerOld
//     })

//     db.Bid.updateOne({_id: bid._id}, { $set: { offer: bid.offer, offerOld: bid.offerOld }})

   
    function getLastAgreedNegotiation(){
        var c = bid.negotiations.communication
        if(bid.status.value === 'NEGOTIATION_FINALIZED' || bid.status.value === 'FINAL_AGREEMENT'){ return c[c.length-1].values}
        
        for(var i = c.length-1; i>=0; i--){
            var n = c[i]
            if(n.from.type === 'SUPPLIER') return n.values
        }
    }
    
    function createNewOffer(){
        var offer = {}
        setRates(offer, lastNeg.rates)
        var primRate = getPrimaryRate(offer)
        setAmenities(offer, lastNeg.amenities, primRate)
        setTaxes(offer, lastNeg.taxes, primRate)
        setTcos(offer, primRate)
        
        return offer
    }
    
    function setRates(offer, rates){
        var seasons = getSeasonsDurations(bid.negotiations.config.seasons)
        var lraSglSum = 0, lraDblSum = 0, nlraSglSum = 0, nlraDblSum = 0, daysSum = 0, seasonsOrd = [1,2,3,4,5]
        
        seasonsOrd.forEach(function (o) {
            var sDur = seasons[o]
            sDur = isNaN(sDur) ? 0 : sDur
            
            var lraSgl = (rates["lraS_s"+o+"_rt1"] || {}).value,
                lraDbl = (rates["lraD_s"+o+"_rt1"] || {}).value,
                nlraSgl = (rates["nlraS_s"+o+"_rt1"] || {}).value,
                nlraDbl = (rates["nlraD_s"+o+"_rt1"] || {}).value
            
            lraSglSum += sDur * ( isNaN(lraSgl) ? 0 : lraSgl )
            lraDblSum += sDur * ( isNaN(lraDbl) ? 0 : lraDbl )
            nlraSglSum += sDur * ( isNaN(nlraSgl) ? 0 : nlraSgl )
            nlraDblSum += sDur * ( isNaN(nlraDbl) ? 0 : nlraDbl )
            daysSum += sDur
        })
        offer.lra = RateValue(daysSum, lraSglSum || lraDblSum)
        offer.nlra = RateValue(daysSum, nlraSglSum || nlraDblSum)
        
        var dyn = rates["dyn"]
        offer.dyn = dyn ? Dynamic(dyn.value) : Unavailable()
    }
    
    function getPrimaryRate(offer){
        if(offer.lra && offer.lra.type === 'FIXED') return offer.lra
        if(offer.nlra && offer.nlra.type === 'FIXED') return offer.nlra
        return false
    }
    
    function setAmenities(offer, old, primRate){
        offer.ec = earlyCheckout(primRate, old.ec)
        offer.prk = fromOld(old.prk)
        offer.bf = breakfast(primRate, old.bf)
        offer.ft = fromOld(old.ft)
        offer.ia = fromOld(old.ia)
        offer.wf = fromOld(old.wf)
        offer.as = fromOld(old.as)
        
        offer.amenitiesTotal = primRate ? total([offer.ec, offer.prk, offer.bf, offer.ft, offer.ia, offer.wf, offer.as]) : Unavailable()
    }
    
    function setTaxes(offer, old, primRate){
        offer.lodging = calculateTax(old.lodging, primRate);
        offer.state = calculateTax(old.state, primRate);
        offer.city = calculateTax(old.city, primRate);
        offer.vatGstRm = calculateTax(old.vatGstRm, primRate);
        offer.vatGstFb = calculateTax(old.vatGstFb, primRate);
        offer.service = calculateTax(old.service, primRate);
        offer.occupancy = calculateTax(old.occupancy, primRate);
        offer.other = calculateTax(old.other, primRate);

        offer.taxesTotal = primRate ? total([offer.lodging, offer.state, offer.city, offer.vatGstRm, offer.vatGstFb, offer.service, offer.occupancy, offer.other]) : Unavailable()
    }
    
    function setTcos(offer, primRate){
        offer.tcos = primRate ? total([primRate, offer.amenitiesTotal, offer.taxesTotal]) : Unavailable()
    }




    function getSeasonsDurations(seasonsConfig){
        var seasonDurations = {}
            
        seasonsConfig.forEach( function (s) {
            seasonDurations[s.id] = calculateSeasonDuration(s.start, s.end)
        })
            
        return seasonDurations;
    }
        
    function calculateSeasonDuration(start, end) {
        var startDate = Date.parse(start), endDate = Date.parse(end),
        dur = Math.ceil( ( endDate - startDate ) / 86400000 )
            
        return isNaN(dur) ? 0 : (dur + 1)
    }

    function RateValue(days, sum){
        return sum && days ? Fixed(sum / days) : Unavailable()
    }
    
    function Fixed(amount){
        return {
            "type" : "FIXED",
            "amount" : Math.round(amount*100) / 100,
            "auxAmount" : 0,
            "isDerived" : false,
            "isIncluded" : false
        }
    }
    
    function Unavailable(){
        return {
            "type" : "UNAVAILABLE",
            "amount" : 0,
            "auxAmount" : 0,
            "isDerived" : false,
            "isIncluded" : false
        }
    }

    function Dynamic(amount){
        return {
            "type" : "PERCENTAGE",
            "amount" : amount,
            "auxAmount" : 0,
            "isDerived" : false,
            "isIncluded" : false
        }
    }
    
    function earlyCheckout(primRate, oldEc){
        var v = fromOld(oldEc)
        calculateAuxiliaryAmount(v, primRate)
        return v
    }
    
    function breakfast(primRate, oldBf){
        var v = fromOld(oldBf)
        v.isDerived && calculateDerivedAmount(v, primRate)
        return v
    }
    
    function fromOld(o){
        if(!o || !o.valid || "UNAVAILABLE" === o.type) return Unavailable()

        var v = {}
        v.type = fromOldType(o.type);
        v.isDerived = o.type === "MOCKED";
        v.amount = o.value;
        v.auxAmount = 0;
        v.isIncluded = o.included;

        if(o.mocked || o.mocked === false){
            v.isDerived = o.mocked;
            if(v.isDerived){
                v.amount = 0;
                v.auxAmount = o.mockPercentage;
            }
        }

        return v
    }

    function fromOldType(oldType){
        switch(oldType){
            case 'MOCKED': 
            case 'FIXED': 
                return 'FIXED'
            case 'PERCENTAGE':
                return 'PERCENTAGE'
            default:
                return 'UNAVAILABLE'
            }
    }
    
    function calculateAuxiliaryAmount(val, rate){
        if(val.type === 'PERCENTAGE' && rate && rate.type === 'FIXED'){
            val.auxAmount = Math.round(val.amount * rate.amount * 100) / 100
        }
    }
    
    function calculateDerivedAmount(val, rate) {
        if(val.isDerived && rate != null && rate.type === 'FIXED'){
            val.type = 'FIXED';
            val.amount = Math.round(val.auxAmount * rate.amount * 100 ) / 100
        }
    }

    function total(values){
        var sum = 0
        values.forEach(function (val) {
            if(val && val.type !== 'UNAVAILABLE' && !val.isIncluded) {
                sum += val.type === 'FIXED' ? val.amount : val.auxAmount
            }
        })
        
        return Fixed(sum)
    }
    
    function calculateTax(old, rate){
        var v = fromOld(old)
        calculateAuxiliaryAmount(v, rate)
        return v
    }

})

```

## NEW QUESTIONNAIRE

Dejan has script files in User folder

Additionally, in Bid collection rfp.questionnaire also needs to be updated OR removed. Otherwise bids cannot load

### RFP

```javascript 1.8
/**
 * !!!! Custom Questions are manually updated => check CustomQuestions function  !!!!

 Functions used to find custom questions is:

 db.getCollection('Bid').find({}).forEach( b => {
    const qModules = b.rfp.questionnaire.model.cells
    const customQuestions = [];
    qModules.forEach( m => {  m.cells.forEach( s => { s !== null && s.cells.forEach( q => { q !== null && /userDefined/i.test(q.id) && customQuestions.push({ question: q, bid: b}) }) }) })

    customQuestions.length && print(customQuestions)
  })

 it found 28 Custom Questions

 db.getCollection('Rfp').find({}).forEach( r => {
    const qModules = r.questionnaire.model.cells
    const customQuestions = [];
    qModules.forEach( m => {  m.cells.forEach( s => { s !== null && s.cells.forEach( q => { q !== null && /userDefined/i.test(q.id) && customQuestions.push({ question: q, rfp: r}) }) }) })

    customQuestions.length && print(customQuestions)
  })

 it found 2 Custom Questions

 Custom Question from Joe is removed on live so it should now report just 1 custom question in RFP

 */

function updatedConfig(filters){
    const rates = readRatesFilter(filters.RATE),
        seasons = [ 1, 2, 3, 4, 5 ],
        roomTypes = readRoomTypesFilter(filters._RT),
        occupancy = readOccupancyFilter(filters.OC)

    return [ rtConfig(), bfdtConfig(), esrtConfig(), grrConfig() ]

    function readRatesFilter(rf){
        const config = [];
        !rf || rf.indexOf("^LRA_") === -1 && config.push("LRA")
        !rf || rf.indexOf("^NLRA_") === -1 && config.push("NLRA")
        !rf || rf.indexOf("^GOVT_") === -1 && config.push("GOVT")
        return config
    }

    function readRoomTypesFilter(rf){
        const config = [ 1, 2, 3 ]
        config.length = 3 - (rf ? rf.length : 0)
        return config
    }

    function readOccupancyFilter(of){
        const config = [];
        !of || of.indexOf("_SGL") === -1 && config.push("SGL")
        !of || of.indexOf("_DBL") === -1 && config.push("DBL")
        return config
    }

    function rtConfig() {
        return {
            "id" : "RT",
            "data" : {
                "rate" : rates,
                "occupancy" : occupancy,
                "rateRules" : { "amount" : "0", "baseRate" : "lastYearRate" },
                "season" : seasons,
                "roomType" : roomTypes
            }
        };
    }

    function bfdtConfig() {
        return {
            "id" : "BFDT",
            "data" : {
                "occupancy" : occupancy,
                "roomType" : roomTypes,
                "blackoutDate" : [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ]
            }
        };
    }

    function esrtConfig() {
        return {
            "id" : "ESRT",
            "data" : {
                "lengthOfStay" : [ 2 ],
                "rate" : rates,
                "occupancy" : occupancy,
                "season" : seasons,
                "roomType" : roomTypes
            }
        };
    }

    function grrConfig() {
        return {
            "id" : "GRR",
            "data" : {
                "season" : seasons
            }
        };
    }
}

function updatedResponse(answers, status, isBid){
    return { answers: updatedAnswers(answers), state: updatedState(Object.keys(answers)), isAvailable: updatedIsAvailable(status, isBid)}

    function updatedAnswers(a){
        if(a.PROPCODE){ a.PROPCODE = a.PROPCODE+'' }
        return a
    }

    function updatedState(a){

        return [ rtState(a), bfdtState(a), esrtState(a), grrState(a) ]

        function extract(arr, regex, group, accStart) {
            return arr.reduce((acc, q) => {
                const m = q.match(regex);
                if (m && m[group]) {
                    const i = parseInt(m[group], 10)
                    return i > acc ? i : acc
                } else {
                    return acc
                }
            }, accStart || 1)
        }

        function rtState(arr){
            const seasons = [1,2,3,4,5],
                roomTypes = [1,2,3]

            seasons.length = extract(arr, /^SEASON([2-5])(START|END)$/ ,1)
            roomTypes.length = extract(arr, /^(LRA|NLRA|GOVT)_S[1-5]_RT([2-3])_(SGL|DBL)$/, 2)
            return { "id" : "RT", "data" : { "season" : seasons, "roomType" : roomTypes } }
        }

        function bfdtState(arr){
            const blackouts = [1,2,3,4,5,6,7,8,9,10],
                roomTypes = [1,2,3]

            blackouts.length = extract(arr, /BD([0-9]{1,2})_RT[1-3]_(SGL|DBL)/, 1)
            roomTypes.length = extract(arr, /^BD[0-9]{1,2}_RT([2-3])_(SGL|DBL)$/, 1)

            return { "id" : "BFDT", "data" : { "roomType" : roomTypes, "blackoutDate" : blackouts } }
        }

        function esrtState(arr){
            const seasons = [1,2,3,4,5],
                roomTypes = [1,2,3],
                los = [2,3,4]

            seasons.length = extract(arr, /^SEASON([2-5])(START|END)_ES$/ ,1)
            roomTypes.length = extract(arr, /^(LRA|NLRA|GOVT)_S[1-5]_RT([2-3])_L[2-4]_ES_(SGL|DBL)$/, 2)
            los.length = extract(arr, /^(LRA|NLRA|GOVT)_S[1-5]_RT[1-3]_L([3-4])_ES_(SGL|DBL)$/, 2, 2)

            return { "id" : "ESRT", "data" : { "lengthOfStay" : los, "season" : seasons, "roomType" : roomTypes } }
        }

        function grrState(arr){
            const seasons = [1,2,3,4,5]
            seasons.length = extract(arr, /^GROUP_S([2-5])_(10-50|51-100)$/ ,1)

            return { "id" : "GRR", "data" : { "season" : seasons } }
        }
    }

    function updatedIsAvailable(status){
        return isBid && ['CREATED', 'DELETED', 'SENT'].indexOf(status) === -1
    }
}

function updatedModel(model){
    cleanupModel(model);
    updateTables(model);
    updateCustomQuestions(model);

    return model

    function cleanupModel(model){

        for(var mi = model.cells.length-1; mi>=0; mi--){
            const module = model.cells[mi];
            if(module === null) {
                model.cells.splice(mi, 1)
            } else {
                cleanModule(module);
                !module.cells.length && model.cells.splice(mi, 1)
            }
        }

        function cleanModule(module) {
            for (var si = module.cells.length; si >= 0; si--) {
                const section = module.cells[si];
                if (section === null || section === undefined) {
                    module.cells.splice(si, 1)
                } else {
                    cleanSection(section);
                    !section.cells.length && module.cells.splice(si, 1)
                }
            }
        }

        function cleanSection(s) {
            if (s && s.cells) {
                for (var qi = s.cells.length; qi >= 0; qi--) {
                    s.cells[qi] === null && s.cells.splice(qi, 1)
                }
            }
        }
    }

    function updateTables(m){
        const cells = m.cells;
        updateRateGrid(cells);
        updateTableSection(cells, "BD", "BFDT", { "ord" : 2, "id" : "BFDT" })
        updateTableSection(cells, "ES", "ESRT", { "ord" : 3, "id" : "ESRT" })
        updateTableSection(cells, "GM", "GRR", { "ord" : 4, "id" : "GRR" })

        function updateRateGrid(cells){
            const module = cells.find(c => c.id === "CS")
            const rtdIndex = module && module.cells.findIndex( c => c.id === "RTD")
            rtdIndex !== false && rtdIndex > -1 && ( module.cells[rtdIndex] = { "ord" : 3, "id" : "RTG" } )

            const rtIndex = module && module.cells.findIndex( c => c.id === "RT")
            if(rtIndex !== false && rtIndex > -1){ module.cells.splice(rtIndex, 1) }
        }

        function updateTableSection(cells, moduleId, sectionId, tableSection) {
            const module = cells.find(c => c.id === moduleId),
                sectionIndex = module && module.cells.findIndex(c => c.id === sectionId)
            sectionIndex !== false && sectionIndex > -1 && ( module.cells[sectionIndex] = tableSection )
        }
    }

    function updateCustomQuestions(model, rfpId){
        if(rfpId === ObjectId("5a1ee6274ddb3925245c8c95")){
            const module = model.cells.find( m => m.id === "CS"),
                section = module && module.cells.find( s => s.id === "CUD")

            const i = section.findIndex( q => q.id === "USERDEFINED1")
            if( i > -1 ){
                section[i] = {
                    "ord" : 244,
                    "id" : "USERDEFINED1",
                    "name" : "Payment Method: Acceptance of Virtual Credit Card (VCC)",
                    "type" : "LIST",
                    "options" : [
                        {
                            "value" : "Y",
                            "label" : "Yes"
                        },
                        {
                            "value" : "N",
                            "label" : "No"
                        }
                    ],
                    "validations" : {
                        "in" : [
                            "Y",
                            "N"
                        ]
                    },
                    "description" : "Following completion of booking, a unique single use Virtual Credit Card (VCC) is deployed to the hotel as the form of payment for the reservation, and can only be utilized with the merchant associated with the secured reservation.",
                    "req" : true
                }
            }
        }
    }
}

function update(questionnaire, answers, answersDraft, status, isBid) {
    const q = JSON.parse(JSON.stringify(questionnaire))

    const newQuestionnaire = {
        type: 'HOTEL',
        config: updatedConfig(rateGridFilter(q.model.cells)),
        response: updatedResponse(answers, status, isBid),
        model: updatedModel(q.model)
    }
    isBid && answersDraft && (newQuestionnaire.responseDraft = updatedResponse(answersDraft, status, isBid))

    return newQuestionnaire

    function rateGridFilter(cells) {
        const module = cells.find(c => c.id === "CS")
        const rt = module && module.cells.find(c => c.id === "RT")
        return rt && rt.filters
    }
}

function run() {
    db.Rfp.find().forEach(rfp => {
        rfp.questionnaireOld = rfp.questionnaire
        rfp.questionnaire = update(rfp.questionnaire, rfp.answers, null, rfp.status.value, false)
        db.Rfp.replaceOne({_id: rfp._id}, rfp)
    })
}
```

### BID

```javascript 1.8
/**
 * !!!! Custom Questions are manually updated => check CustomQuestions function  !!!!

 Functions used to find custom questions is:

 db.getCollection('Bid').find({}).forEach( b => {
    const qModules = b.rfp.questionnaire.model.cells
    const customQuestions = [];
    qModules.forEach( m => {  m.cells.forEach( s => { s !== null && s.cells.forEach( q => { q !== null && /userDefined/i.test(q.id) && customQuestions.push({ question: q, bid: b}) }) }) })

    customQuestions.length && print(customQuestions)
  })

 it found 28 Custom Questions

 db.getCollection('Rfp').find({}).forEach( r => {
    const qModules = r.questionnaire.model.cells
    const customQuestions = [];
    qModules.forEach( m => {  m.cells.forEach( s => { s !== null && s.cells.forEach( q => { q !== null && /userDefined/i.test(q.id) && customQuestions.push({ question: q, rfp: r}) }) }) })

    customQuestions.length && print(customQuestions)
  })

 it found 2 Custom Questions

 Custom Question from Joe is removed on live so it should now report just 1 custom question in RFP

 */

function updatedConfig(filters){
    const rates = readRatesFilter(filters.RATE),
        seasons = [ 1, 2, 3, 4, 5 ],
        roomTypes = readRoomTypesFilter(filters._RT),
        occupancy = readOccupancyFilter(filters.OC)

    return [ rtConfig(), bfdtConfig(), esrtConfig(), grrConfig() ]

    function readRatesFilter(rf){
        const config = [];
        !rf || rf.indexOf("^LRA_") === -1 && config.push("LRA")
        !rf || rf.indexOf("^NLRA_") === -1 && config.push("NLRA")
        !rf || rf.indexOf("^GOVT_") === -1 && config.push("GOVT")
        return config
    }

    function readRoomTypesFilter(rf){
        const config = [ 1, 2, 3 ]
        config.length = 3 - (rf ? rf.length : 0)
        return config
    }

    function readOccupancyFilter(of){
        const config = [];
        !of || of.indexOf("_SGL") === -1 && config.push("SGL")
        !of || of.indexOf("_DBL") === -1 && config.push("DBL")
        return config
    }

    function rtConfig() {
        return {
            "id" : "RT",
            "data" : {
                "rate" : rates,
                "occupancy" : occupancy,
                "rateRules" : { "amount" : "0", "baseRate" : "lastYearRate" },
                "season" : seasons,
                "roomType" : roomTypes
            }
        };
    }

    function bfdtConfig() {
        return {
            "id" : "BFDT",
            "data" : {
                "occupancy" : occupancy,
                "roomType" : roomTypes,
                "blackoutDate" : [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ]
            }
        };
    }

    function esrtConfig() {
        return {
            "id" : "ESRT",
            "data" : {
                "lengthOfStay" : [ 2 ],
                "rate" : rates,
                "occupancy" : occupancy,
                "season" : seasons,
                "roomType" : roomTypes
            }
        };
    }

    function grrConfig() {
        return {
            "id" : "GRR",
            "data" : {
                "season" : seasons
            }
        };
    }
}

function updatedResponse(answers, status, isBid){
    return { answers: updatedAnswers(answers), state: updatedState(Object.keys(answers)), isAvailable: updatedIsAvailable(status, isBid)}

    function updatedAnswers(a){
        if(a.PROPCODE){ a.PROPCODE = a.PROPCODE+'' }
        return a
    }

    function updatedState(a){

        return [ rtState(a), bfdtState(a), esrtState(a), grrState(a) ]

        function extract(arr, regex, group, accStart) {
            return arr.reduce((acc, q) => {
                const m = q.match(regex);
                if (m && m[group]) {
                    const i = parseInt(m[group], 10)
                    return i > acc ? i : acc
                } else {
                    return acc
                }
            }, accStart || 1)
        }

        function rtState(arr){
            const seasons = [1,2,3,4,5],
                roomTypes = [1,2,3]

            seasons.length = extract(arr, /^SEASON([2-5])(START|END)$/ ,1)
            roomTypes.length = extract(arr, /^(LRA|NLRA|GOVT)_S[1-5]_RT([2-3])_(SGL|DBL)$/, 2)
            return { "id" : "RT", "data" : { "season" : seasons, "roomType" : roomTypes } }
        }

        function bfdtState(arr){
            const blackouts = [1,2,3,4,5,6,7,8,9,10],
                roomTypes = [1,2,3]

            blackouts.length = extract(arr, /BD([0-9]{1,2})_RT[1-3]_(SGL|DBL)/, 1)
            roomTypes.length = extract(arr, /^BD[0-9]{1,2}_RT([2-3])_(SGL|DBL)$/, 1)

            return { "id" : "BFDT", "data" : { "roomType" : roomTypes, "blackoutDate" : blackouts } }
        }

        function esrtState(arr){
            const seasons = [1,2,3,4,5],
                roomTypes = [1,2,3],
                los = [2,3,4]

            seasons.length = extract(arr, /^SEASON([2-5])(START|END)_ES$/ ,1)
            roomTypes.length = extract(arr, /^(LRA|NLRA|GOVT)_S[1-5]_RT([2-3])_L[2-4]_ES_(SGL|DBL)$/, 2)
            los.length = extract(arr, /^(LRA|NLRA|GOVT)_S[1-5]_RT[1-3]_L([3-4])_ES_(SGL|DBL)$/, 2, 2)

            return { "id" : "ESRT", "data" : { "lengthOfStay" : los, "season" : seasons, "roomType" : roomTypes } }
        }

        function grrState(arr){
            const seasons = [1,2,3,4,5]
            seasons.length = extract(arr, /^GROUP_S([2-5])_(10-50|51-100)$/ ,1)

            return { "id" : "GRR", "data" : { "season" : seasons } }
        }
    }

    function updatedIsAvailable(status){
        return isBid && ['CREATED', 'DELETED', 'SENT'].indexOf(status) === -1
    }
}

function updatedModel(model){
    cleanupModel(model);
    updateTables(model);
    updateCustomQuestions(model);

    return model

    function cleanupModel(model){

        for(var mi = model.cells.length-1; mi>=0; mi--){
            const module = model.cells[mi];
            if(module === null) {
                model.cells.splice(mi, 1)
            } else {
                cleanModule(module);
                !module.cells.length && model.cells.splice(mi, 1)
            }
        }

        function cleanModule(module) {
            for (var si = module.cells.length; si >= 0; si--) {
                const section = module.cells[si];
                if (section === null || section === undefined) {
                    module.cells.splice(si, 1)
                } else {
                    cleanSection(section);
                    !section.cells.length && module.cells.splice(si, 1)
                }
            }
        }

        function cleanSection(s) {
            if (s && s.cells) {
                for (var qi = s.cells.length; qi >= 0; qi--) {
                    s.cells[qi] === null && s.cells.splice(qi, 1)
                }
            }
        }
    }

    function updateTables(m){
        const cells = m.cells;
        updateRateGrid(cells);
        updateTableSection(cells, "BD", "BFDT", { "ord" : 2, "id" : "BFDT" })
        updateTableSection(cells, "ES", "ESRT", { "ord" : 3, "id" : "ESRT" })
        updateTableSection(cells, "GM", "GRR", { "ord" : 4, "id" : "GRR" })

        function updateRateGrid(cells){
            const module = cells.find(c => c.id === "CS")
            const rtdIndex = module && module.cells.findIndex( c => c.id === "RTD")
            rtdIndex !== false && rtdIndex > -1 && ( module.cells[rtdIndex] = { "ord" : 3, "id" : "RTG" } )

            const rtIndex = module && module.cells.findIndex( c => c.id === "RT")
            if(rtIndex !== false && rtIndex > -1){ module.cells.splice(rtIndex, 1) }
        }

        function updateTableSection(cells, moduleId, sectionId, tableSection) {
            const module = cells.find(c => c.id === moduleId),
                sectionIndex = module && module.cells.findIndex(c => c.id === sectionId)
            sectionIndex !== false && sectionIndex > -1 && ( module.cells[sectionIndex] = tableSection )
        }
    }

    function updateCustomQuestions(model, rfpId){
        if(rfpId === ObjectId("5a1ee6274ddb3925245c8c95")){
            const module = model.cells.find( m => m.id === "CS"),
                section = module && module.cells.find( s => s.id === "CUD")

            const i = section.findIndex( q => q.id === "USERDEFINED1")
            if( i > -1 ){
                section[i] = {
                    "ord" : 244,
                    "id" : "USERDEFINED1",
                    "name" : "Payment Method: Acceptance of Virtual Credit Card (VCC)",
                    "type" : "LIST",
                    "options" : [
                        {
                            "value" : "Y",
                            "label" : "Yes"
                        },
                        {
                            "value" : "N",
                            "label" : "No"
                        }
                    ],
                    "validations" : {
                        "in" : [
                            "Y",
                            "N"
                        ]
                    },
                    "description" : "Following completion of booking, a unique single use Virtual Credit Card (VCC) is deployed to the hotel as the form of payment for the reservation, and can only be utilized with the merchant associated with the secured reservation.",
                    "req" : true
                }
            }
        }
    }
}

function update(questionnaire, answers, answersDraft, status, isBid) {
    const q = JSON.parse(JSON.stringify(questionnaire))
    const newQuestionnaire = {
        type: 'HOTEL',
        config: updatedConfig(rateGridFilter(q.model.cells)),
        response: updatedResponse(answers, status, isBid),
        model: updatedModel(q.model)
    }
    isBid && answersDraft && (newQuestionnaire.responseDraft = updatedResponse(answersDraft, status, isBid))

    return newQuestionnaire

    function rateGridFilter(cells) {
        const module = cells.find(c => c.id === "CS")
        const rt = module && module.cells.find(c => c.id === "RT")
        return rt && rt.filters
    }
}

function run() {
    db.Bid.find().forEach(bid => {
        bid.questionnaireOld = bid.rfp.questionnaire
        bid.questionnaire = update(bid.rfp.questionnaire, bid.answers, bid.answersDraft, bid.status.value, true)
        db.Bid.replaceOne({_id: bid._id}, bid)
    })
}
```

### Default Response

```javascript 1.8
function updatedResponse(answers, status, isBid){
    return { answers: updatedAnswers(answers), state: updatedState(Object.keys(answers)), isAvailable: updatedIsAvailable(status, isBid)}

    function updatedAnswers(a){
        if(a.PROPCODE){ a.PROPCODE = a.PROPCODE+'' }
        return a
    }

    function updatedState(a){

        return [ rtState(a), bfdtState(a), esrtState(a), grrState(a) ]

        function extract(arr, regex, group, accStart) {
            return arr.reduce((acc, q) => {
                const m = q.match(regex);
                if (m && m[group]) {
                    const i = parseInt(m[group], 10)
                    return i > acc ? i : acc
                } else {
                    return acc
                }
            }, accStart || 1)
        }

        function rtState(arr){
            const seasons = [1,2,3,4,5],
                roomTypes = [1,2,3]

            seasons.length = extract(arr, /^SEASON([2-5])(START|END)$/ ,1)
            roomTypes.length = extract(arr, /^(LRA|NLRA|GOVT)_S[1-5]_RT([2-3])_(SGL|DBL)$/, 2)
            return { "id" : "RT", "data" : { "season" : seasons, "roomType" : roomTypes } }
        }

        function bfdtState(arr){
            const blackouts = [1,2,3,4,5,6,7,8,9,10],
                roomTypes = [1,2,3]

            blackouts.length = extract(arr, /BD([0-9]{1,2})_RT[1-3]_(SGL|DBL)/, 1)
            roomTypes.length = extract(arr, /^BD[0-9]{1,2}_RT([2-3])_(SGL|DBL)$/, 1)

            return { "id" : "BFDT", "data" : { "roomType" : roomTypes, "blackoutDate" : blackouts } }
        }

        function esrtState(arr){
            const seasons = [1,2,3,4,5],
                roomTypes = [1,2,3],
                los = [2,3,4]

            seasons.length = extract(arr, /^SEASON([2-5])(START|END)_ES$/ ,1)
            roomTypes.length = extract(arr, /^(LRA|NLRA|GOVT)_S[1-5]_RT([2-3])_L[2-4]_ES_(SGL|DBL)$/, 2)
            los.length = extract(arr, /^(LRA|NLRA|GOVT)_S[1-5]_RT[1-3]_L([3-4])_ES_(SGL|DBL)$/, 2, 2)

            return { "id" : "ESRT", "data" : { "lengthOfStay" : los, "season" : seasons, "roomType" : roomTypes } }
        }

        function grrState(arr){
            const seasons = [1,2,3,4,5]
            seasons.length = extract(arr, /^GROUP_S([2-5])_(10-50|51-100)$/ ,1)

            return { "id" : "GRR", "data" : { "season" : seasons } }
        }
    }

    function updatedIsAvailable(status){
        return isBid && ['CREATED', 'DELETED', 'SENT'].indexOf(status) === -1
    }
}

function update(questionnaire, answers, answersDraft, status, isBid) {
    const q = JSON.parse(JSON.stringify(questionnaire))
    const newQuestionnaire = {
        type: 'HOTEL',
        config: updatedConfig(rateGridFilter(q.model.cells)),
        response: updatedResponse(answers, status, isBid),
        model: updatedModel(q.model)
    }
    isBid && answersDraft && (newQuestionnaire.responseDraft = updatedResponse(answersDraft, status, isBid))

    return newQuestionnaire

    function rateGridFilter(cells) {
        const module = cells.find(c => c.id === "CS")
        const rt = module && module.cells.find(c => c.id === "RT")
        return rt && rt.filters
    }
}

function run() {
    db.RfpDefaultAnswers.find().forEach(a => {
        const response = updatedResponse(a.answers, "", false)
        response._id = a._id
        response.isAvailable = true
        db.RfpDefaultResponse.insert(response)
    })
}

```

## Feature Bid Manager Group and Filter on Countries, States, Regions, Cities

### Affected Collections:

1. BidManagerView
1. Bid

#### Database Patches

##### BidManagerView Collection
```javascript
db.getCollection('BidManagerView').updateMany({}, {$push: {columns: {
    $each: [ "hotelRfpBid.hotel.city", "hotelRfpBid.hotel.state_region", "hotelRfpBid.hotel.country" ],
    $position: 0
    }}})  
```
##### Bid Collection
```javascript
var COUNTRIES = {
  "AF": "Afghanistan",
  "AL": "Albania",
  "DZ": "Algeria",
  "AS": "American Samoa",
  "AD": "Andorra",
  "AO": "Angola",
  "AI": "Anguilla",
  "AG": "Antigua And Barbuda",
  "AR": "Argentina",
  "AM": "Armenia",
  "AW": "Aruba",
  "AC": "Ascension Island",
  "AU": "Australia",
  "AT": "Austria",
  "AZ": "Azerbaijan",
  "BS": "Bahamas",
  "BH": "Bahrain",
  "BD": "Bangladesh",
  "BB": "Barbados",
  "BY": "Belarus",
  "BE": "Belgium",
  "BZ": "Belize",
  "BJ": "Benin",
  "BM": "Bermuda",
  "BT": "Bhutan",
  "BO": "Bolivia",
  "BA": "Bosnia And Herzegovina",
  "BW": "Botswana",
  "BR": "Brazil",
  "IO": "British Indian Ocean Territory",
  "VG": "British Virgin Islands",
  "BN": "Brunei",
  "BG": "Bulgaria",
  "BF": "Burkina Faso",
  "BI": "Burundi",
  "KH": "Cambodia",
  "CM": "Cameroon",
  "CA": "Canada",
  "CV": "Cape Verde",
  "AN": "Caribbean Netherlands",
  "KY": "Cayman Islands",
  "CF": "Central African Republic",
  "TD": "Chad",
  "CL": "Chile",
  "CN": "China",
  "CO": "Colombia",
  "KM": "Comoros",
  "CD": "Congo",
  "CG": "Congo",
  "CK": "Cook Islands",
  "CR": "Costa Rica",
  "CI": "Côte d’Ivoire",
  "HR": "Croatia",
  "CU": "Cuba",
  "CW": "Curaçao",
  "CY": "Cyprus",
  "CZ": "Czech Republic",
  "DK": "Denmark",
  "DJ": "Djibouti",
  "DM": "Dominica",
  "DO": "Dominican Republic",
  "EC": "Ecuador",
  "EG": "Egypt",
  "SV": "El Salvador",
  "GQ": "Equatorial Guinea",
  "ER": "Eritrea",
  "EE": "Estonia",
  "ET": "Ethiopia",
  "FK": "Falkland Islands",
  "FO": "Faroe Islands",
  "FJ": "Fiji",
  "FI": "Finland",
  "FR": "France",
  "GF": "French Guiana",
  "PF": "French Polynesia",
  "GA": "Gabon",
  "GM": "Gambia",
  "GE": "Georgia",
  "DE": "Germany",
  "GH": "Ghana",
  "GI": "Gibraltar",
  "GR": "Greece",
  "GL": "Greenland",
  "GD": "Grenada",
  "GP": "Guadeloupe",
  "GU": "Guam",
  "GT": "Guatemala",
  "GN": "Guinea",
  "GW": "Guinea-Bissau",
  "GY": "Guyana",
  "HT": "Haiti",
  "HN": "Honduras",
  "HK": "Hong Kong",
  "HU": "Hungary",
  "IS": "Iceland",
  "IN": "India",
  "ID": "Indonesia",
  "IR": "Iran",
  "IQ": "Iraq",
  "IE": "Ireland",
  "IL": "Israel",
  "IT": "Italy",
  "JM": "Jamaica",
  "JP": "Japan",
  "JO": "Jordan",
  "KZ": "Kazakhstan",
  "KE": "Kenya",
  "KI": "Kiribati",
  "KW": "Kuwait",
  "KG": "Kyrgyzstan",
  "LA": "Laos",
  "LV": "Latvia",
  "LB": "Lebanon",
  "LS": "Lesotho",
  "LR": "Liberia",
  "LY": "Libya",
  "LI": "Liechtenstein",
  "LT": "Lithuania",
  "LU": "Luxembourg",
  "MO": "Macau",
  "MK": "Macedonia",
  "MG": "Madagascar",
  "MW": "Malawi",
  "MY": "Malaysia",
  "MV": "Maldives",
  "ML": "Mali",
  "MT": "Malta",
  "MH": "Marshall Islands",
  "MQ": "Martinique",
  "MR": "Mauritania",
  "MU": "Mauritius",
  "MX": "Mexico",
  "FM": "Micronesia",
  "MD": "Moldova",
  "MC": "Monaco",
  "MN": "Mongolia",
  "ME": "Montenegro",
  "MS": "Montserrat",
  "MA": "Morocco",
  "MZ": "Mozambique",
  "MM": "Myanmar",
  "NA": "Namibia",
  "NR": "Nauru",
  "NP": "Nepal",
  "NL": "Netherlands",
  "NC": "New Caledonia",
  "NZ": "New Zealand",
  "NI": "Nicaragua",
  "NE": "Niger",
  "NG": "Nigeria",
  "NU": "Niue",
  "NF": "Norfolk Island",
  "MP": "Northern Mariana Islands",
  "KP": "North Korea",
  "NO": "Norway",
  "OM": "Oman",
  "PK": "Pakistan",
  "PW": "Palau",
  "PS": "Palestine",
  "PA": "Panama",
  "PG": "Papua New Guinea",
  "PY": "Paraguay",
  "PE": "Peru",
  "PH": "Philippines",
  "PL": "Poland",
  "PT": "Portugal",
  "PR": "Puerto Rico",
  "QA": "Qatar",
  "RE": "Réunion",
  "RO": "Romania",
  "RU": "Russia",
  "RW": "Rwanda",
  "WS": "Samoa",
  "SM": "San Marino",
  "ST": "São Tomé And Príncipe",
  "SA": "Saudi Arabia",
  "SN": "Senegal",
  "RS": "Serbia",
  "SC": "Seychelles",
  "SL": "Sierra Leone",
  "SG": "Singapore",
  "SX": "Sint Maarten",
  "SK": "Slovakia",
  "SI": "Slovenia",
  "SB": "Solomon Islands",
  "SO": "Somalia",
  "ZA": "South Africa",
  "KR": "South Korea",
  "SS": "South Sudan",
  "ES": "Spain",
  "LK": "Sri Lanka",
  "BL": "St. Barthélemy",
  "SH": "St. Helena",
  "KN": "St. Kitts And Nevis",
  "LC": "St. Lucia",
  "MF": "St. Martin",
  "PM": "St. Pierre And Miquelon",
  "VC": "St. Vincent And Grenadines",
  "SD": "Sudan",
  "SR": "Suriname",
  "SZ": "Swaziland",
  "SE": "Sweden",
  "CH": "Switzerland",
  "SY": "Syria",
  "TW": "Taiwan",
  "TJ": "Tajikistan",
  "TZ": "Tanzania",
  "TH": "Thailand",
  "TL": "Timor-Leste",
  "TG": "Togo",
  "TK": "Tokelau",
  "TO": "Tonga",
  "TT": "Trinidad And Tobago",
  "TN": "Tunisia",
  "TR": "Turkey",
  "TM": "Turkmenistan",
  "TC": "Turks And Caicos Islands",
  "TV": "Tuvalu",
  "VI": "U.S. Virgin Islands",
  "UG": "Uganda",
  "UA": "Ukraine",
  "AE": "United Arab Emirates",
  "GB": "United Kingdom",
  "US": "United States",
  "UY": "Uruguay",
  "UZ": "Uzbekistan",
  "VU": "Vanuatu",
  "VA": "Vatican City",
  "VE": "Venezuela",
  "VN": "Vietnam",
  "WF": "Wallis And Futuna",
  "YE": "Yemen",
  "ZM": "Zambia",
  "ZW": "Zimbabwe"
}

var STATES = {
  "AL": "Alabama",
  "AK": "Alaska",
  "AB": "Alberta",
  "AS": "American Samoa",
  "AZ": "Arizona",
  "AR": "Arkansas",
  "BC": "British Columbia",
  "CA": "California",
  "CO": "Colorado",
  "CT": "Connecticut",
  "DE": "Delaware",
  "DC": "District of Columbia",
  "FM": "Federated States of Micronesia",
  "FL": "Florida",
  "GA": "Georgia",
  "GU": "Guam",
  "HI": "Hawaii",
  "ID": "Idaho",
  "IL": "Illinois",
  "IN": "Indiana",
  "IA": "Iowa",
  "KS": "Kansas",
  "KY": "Kentucky",
  "LA": "Louisiana",
  "ME": "Maine",
  "MB": "Manitoba",
  "MH": "Marshall Islands",
  "MD": "Maryland",
  "MA": "Massachusetts",
  "MI": "Michigan",
  "MN": "Minnesota",
  "MS": "Mississippi",
  "MO": "Missouri",
  "MT": "Montana",
  "NE": "Nebraska",
  "NV": "Nevada",
  "NB": "New Brunswick",
  "NH": "New Hampshire",
  "NJ": "New Jersey",
  "NM": "New Mexico",
  "NY": "New York",
  "NL": "Newfoundland",
  "NC": "North Carolina",
  "ND": "North Dakota",
  "MP": "Northern Mariana Islands",
  "NT": "Northwest Territories",
  "NS": "Nova Scotia",
  "NU": "Nunavut",
  "OH": "Ohio",
  "OK": "Oklahoma",
  "ON": "Ontario",
  "OR": "Oregon",
  "PW": "Palau",
  "PA": "Pennsylvania",
  "PE": "Prince Edward Island",
  "PR": "Puerto Rico",
  "QC": "Quebec",
  "RI": "Rhode Island",
  "SK": "Saskatchewan",
  "SC": "South Carolina",
  "SD": "South Dakota",
  "TN": "Tennessee",
  "TX": "Texas",
  "UT": "Utah",
  "VT": "Vermont",
  "VI": "Virgin Islands",
  "VA": "Virginia",
  "WA": "Washington",
  "WV": "West Virginia",
  "WI": "Wisconsin",
  "WY": "Wyoming",
  "YT": "Yukon Territory"
}

function toTitleCase(str)
{
    return str.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
}  

db.getCollection('Bid').find({}).forEach( function( bid ) { 
    
    var address = bid.supplier.company.location.address;
    var addressStateRegion = address.state ? STATES[address.state] : address.region;
    var stateRegion = addressStateRegion && addressStateRegion.length ? addressStateRegion + ", " + address.country : COUNTRIES[address.country]
   
    
    var addressAnalytics = {
       country: COUNTRIES[address.country],
       stateRegion: stateRegion,
       city: toTitleCase(address.city) + ", " + ( addressStateRegion ? addressStateRegion + ", " : "") + COUNTRIES[address.country]
   }
   
    db.Bid.updateOne({_id: bid._id}, {$set: {"analytics.address": addressAnalytics}})
    } )
    
```

## Feature Bid Manager Currency
Add Currency as column to all Bid Manager Views

### Affected Collections:

1. BidManagerView

#### Database Patch

##### BidManagerView Collection
```javascript
db.BidManagerView.find({}).forEach( function (bmView) {
    var currencyIndex = bmView.columns.indexOf('bid.currency');
    if(currencyIndex === -1) {
        var lraIndex = bmView.columns.indexOf('hotelRfpBid.lra');
    
        if(lraIndex !== -1 ){
            bmView.columns.splice(lraIndex, 0, 'bid.currency')
        } else {
            printjson({'error': bmView._id})
        }
  
        db.BidManagerView.replaceOne( {_id: bmView._id}, bmView);
    }
})
```
 

## Feature mobile2phone (applied to live)
User *mobile* field is renamed to *phone*

### Affected Collections:

1. Bid
1. Invitation
1. Rfp
1. RfpTemplate
1. User

To update *User* and *Invitation* Collections, rename *mobile* field to *phone*.

To update *RfpTemplate* Collection, cover letter and final agreement templates must be update *{buyer.contact.mobile}* and *{supplier.contact.mobile}* to *{buyer.contact.phone}* and *{supplier.contact.phone}*.

To update *Rfp* and *Bid* Collections:

- cover letter and final agreement templates must be updated as in *RfpTemplate* Collection update
- *buyer.contact.mobile* should be renamed to *buyer.contact.phone*
- *supplier.contact.mobile* should be renamed to *supplier.contact.phone*

#### Database Patches

##### Bid Collection (applied to live)
```javascript
db.Bid.find({}).forEach( function (bid) {
    bid.rfp.coverLetter = updateLetter(bid.rfp.coverLetter);
    bid.rfp.finalAgreement = updateLetter(bid.rfp.finalAgreement);
    updateUser(bid.buyer.contact);
    if(bid.supplier && bid.supplier.contact && bid.supplier.contact.mobile) {
        updateUser(bid.supplier.contact);
    }
    
    db.Bid.replaceOne( {_id: bid._id}, bid);
    
    function updateLetter(letter){
        var l = letter.replace(/{{buyer.contact.mobile}}/g, '{{buyer.contact.phone}}');
        return l.replace(/{{supplier.contact.mobile}}/g, '{{supplier.contact.phone}}'); 
    }
    
    function updateUser(user){
        user.phone = user.mobile
        delete user.mobile
    }
})
```

##### Invitation Collection (applied to live)
```javascript
db.Invitation.find({}).forEach( function (invitation) {
    if(invitation.mobile){
        updateUser(invitation)
        db.Invitation.replaceOne( {_id: invitation._id}, invitation);
    }
   
    function updateUser(user){
        user.phone = user.mobile
        delete user.mobile
    }
})
```

##### Rfp Collection (applied to live)
```javascript
db.Rfp.find({}).forEach( function (rfp) {
    rfp.coverLetter = updateLetter(rfp.coverLetter);
    rfp.finalAgreement = updateLetter(rfp.finalAgreement);
    updateUser(rfp.specifications.buyer.contact);
    
    db.Rfp.replaceOne( {_id: rfp._id}, rfp);
    
    function updateLetter(letter){
        var l = letter.replace(/{{buyer.contact.mobile}}/g, '{{buyer.contact.phone}}');
        return l.replace(/{{supplier.contact.mobile}}/g, '{{supplier.contact.phone}}'); 
    }
    
    function updateUser(user){
        user.phone = user.mobile
        delete user.mobile
    }
})
```

##### RfpTemplate Collection (RfpTemplate Collection is copied, not updated)
```javascript
db.RfpTemplate.find({}).forEach( function (rfpTemplate) {
    rfpTemplate.coverLetter = updateLetter(rfpTemplate.coverLetter);
    rfpTemplate.finalAgreement = updateLetter(rfpTemplate.finalAgreement);
    
    db.RfpTemplate.replaceOne( {_id: rfpTemplate._id}, rfpTemplate);
    
    function updateLetter(letter){
        var l = letter.replace(/{{buyer.contact.mobile}}/g, '{{buyer.contact.phone}}');
        return l.replace(/{{supplier.contact.mobile}}/g, '{{supplier.contact.phone}}');    }
})
```

##### User Collection (applied to live)

```javascript
db.User.find({}).forEach( function (user) {
    if(user.mobile){
        updateUser(user)
        db.User.replaceOne( {_id: user._id}, user);
    }
   
    function updateUser(user){
        user.phone = user.mobile
        delete user.mobile
    }
})
```
    
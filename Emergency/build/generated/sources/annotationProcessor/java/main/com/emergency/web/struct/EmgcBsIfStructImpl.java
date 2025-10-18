package com.emergency.web.struct;

import com.emergency.web.dto.response.emgc.EmgcBsIfResponseDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-28T18:14:53+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.2.jar, environment: Java 17.0.11 (Eclipse Adoptium)"
)
@Component
public class EmgcBsIfStructImpl implements EmgcBsIfStruct {

    @Override
    public EmgcBsIfResponseDto toDto(EmgcBsIfResponseDto.Item item) {
        if ( item == null ) {
            return null;
        }

        EmgcBsIfResponseDto.EmgcBsIfResponseDtoBuilder emgcBsIfResponseDto = EmgcBsIfResponseDto.builder();

        emgcBsIfResponseDto.hpid( item.getHpid() );
        emgcBsIfResponseDto.dutyName( item.getDutyName() );
        emgcBsIfResponseDto.postCdn1( item.getPostCdn1() );
        emgcBsIfResponseDto.postCdn2( item.getPostCdn2() );
        emgcBsIfResponseDto.dutyAddr( item.getDutyAddr() );
        emgcBsIfResponseDto.dutyTel1( item.getDutyTel1() );
        emgcBsIfResponseDto.dutyTel3( item.getDutyTel3() );
        emgcBsIfResponseDto.hvec( item.getHvec() );
        emgcBsIfResponseDto.hvoc( item.getHvoc() );
        emgcBsIfResponseDto.hvcc( item.getHvcc() );
        emgcBsIfResponseDto.hvncc( item.getHvncc() );
        emgcBsIfResponseDto.hvccc( item.getHvccc() );
        emgcBsIfResponseDto.hvicc( item.getHvicc() );
        emgcBsIfResponseDto.hvgc( item.getHvgc() );
        emgcBsIfResponseDto.dutyHayn( item.getDutyHayn() );
        emgcBsIfResponseDto.dutyHano( item.getDutyHano() );
        emgcBsIfResponseDto.dutyInf( item.getDutyInf() );
        emgcBsIfResponseDto.dutyMapimg( item.getDutyMapimg() );
        emgcBsIfResponseDto.dutyEryn( item.getDutyEryn() );
        emgcBsIfResponseDto.dutyTime1c( item.getDutyTime1c() );
        emgcBsIfResponseDto.dutyTime2c( item.getDutyTime2c() );
        emgcBsIfResponseDto.dutyTime3c( item.getDutyTime3c() );
        emgcBsIfResponseDto.dutyTime4c( item.getDutyTime4c() );
        emgcBsIfResponseDto.dutyTime5c( item.getDutyTime5c() );
        emgcBsIfResponseDto.dutyTime6c( item.getDutyTime6c() );
        emgcBsIfResponseDto.dutyTime7c( item.getDutyTime7c() );
        emgcBsIfResponseDto.dutyTime8c( item.getDutyTime8c() );
        emgcBsIfResponseDto.dutyTime1s( item.getDutyTime1s() );
        emgcBsIfResponseDto.dutyTime2s( item.getDutyTime2s() );
        emgcBsIfResponseDto.dutyTime3s( item.getDutyTime3s() );
        emgcBsIfResponseDto.dutyTime4s( item.getDutyTime4s() );
        emgcBsIfResponseDto.dutyTime5s( item.getDutyTime5s() );
        emgcBsIfResponseDto.dutyTime6s( item.getDutyTime6s() );
        emgcBsIfResponseDto.dutyTime7s( item.getDutyTime7s() );
        emgcBsIfResponseDto.dutyTime8s( item.getDutyTime8s() );
        emgcBsIfResponseDto.MKioskTy25( item.getMKioskTy25() );
        emgcBsIfResponseDto.MKioskTy1( item.getMKioskTy1() );
        emgcBsIfResponseDto.MKioskTy2( item.getMKioskTy2() );
        emgcBsIfResponseDto.MKioskTy3( item.getMKioskTy3() );
        emgcBsIfResponseDto.MKioskTy4( item.getMKioskTy4() );
        emgcBsIfResponseDto.MKioskTy5( item.getMKioskTy5() );
        emgcBsIfResponseDto.MKioskTy6( item.getMKioskTy6() );
        emgcBsIfResponseDto.MKioskTy7( item.getMKioskTy7() );
        emgcBsIfResponseDto.MKioskTy8( item.getMKioskTy8() );
        emgcBsIfResponseDto.MKioskTy9( item.getMKioskTy9() );
        emgcBsIfResponseDto.MKioskTy10( item.getMKioskTy10() );
        emgcBsIfResponseDto.MKioskTy11( item.getMKioskTy11() );
        emgcBsIfResponseDto.wgs84Lon( item.getWgs84Lon() );
        emgcBsIfResponseDto.wgs84Lat( item.getWgs84Lat() );
        emgcBsIfResponseDto.dgidIdName( item.getDgidIdName() );
        emgcBsIfResponseDto.hpbdn( item.getHpbdn() );
        emgcBsIfResponseDto.hpccuyn( item.getHpccuyn() );
        emgcBsIfResponseDto.hpcuyn( item.getHpcuyn() );
        emgcBsIfResponseDto.hperyn( item.getHperyn() );
        emgcBsIfResponseDto.hpgryn( item.getHpgryn() );
        emgcBsIfResponseDto.hpicuyn( item.getHpicuyn() );
        emgcBsIfResponseDto.hpnicuyn( item.getHpnicuyn() );
        emgcBsIfResponseDto.hpopyn( item.getHpopyn() );

        return emgcBsIfResponseDto.build();
    }
}

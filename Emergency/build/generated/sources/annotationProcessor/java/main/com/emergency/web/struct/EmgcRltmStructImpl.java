package com.emergency.web.struct;

import com.emergency.web.dto.response.emgc.EmgcRltmResponseDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-28T18:14:53+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.2.jar, environment: Java 17.0.11 (Eclipse Adoptium)"
)
@Component
public class EmgcRltmStructImpl implements EmgcRltmStruct {

    @Override
    public EmgcRltmResponseDto toDto(EmgcRltmResponseDto.Item item) {
        if ( item == null ) {
            return null;
        }

        EmgcRltmResponseDto.EmgcRltmResponseDtoBuilder emgcRltmResponseDto = EmgcRltmResponseDto.builder();

        emgcRltmResponseDto.rnum( item.getRnum() );
        emgcRltmResponseDto.hpid( item.getHpid() );
        emgcRltmResponseDto.phpid( item.getPhpid() );
        emgcRltmResponseDto.hvidate( item.getHvidate() );
        if ( item.getHvec() != null ) {
            emgcRltmResponseDto.hvec( item.getHvec() );
        }
        if ( item.getHvoc() != null ) {
            emgcRltmResponseDto.hvoc( item.getHvoc() );
        }
        if ( item.getHvcc() != null ) {
            emgcRltmResponseDto.hvcc( item.getHvcc() );
        }
        if ( item.getHvncc() != null ) {
            emgcRltmResponseDto.hvncc( item.getHvncc() );
        }
        if ( item.getHvccc() != null ) {
            emgcRltmResponseDto.hvccc( item.getHvccc() );
        }
        if ( item.getHvicc() != null ) {
            emgcRltmResponseDto.hvicc( item.getHvicc() );
        }
        if ( item.getHvgc() != null ) {
            emgcRltmResponseDto.hvgc( item.getHvgc() );
        }
        emgcRltmResponseDto.hvdnm( item.getHvdnm() );
        emgcRltmResponseDto.hvctayn( item.getHvctayn() );
        emgcRltmResponseDto.hvmriayn( item.getHvmriayn() );
        emgcRltmResponseDto.hvangioayn( item.getHvangioayn() );
        emgcRltmResponseDto.hvventiayn( item.getHvventiayn() );
        emgcRltmResponseDto.hvventisoayn( item.getHvventisoayn() );
        emgcRltmResponseDto.hvincuayn( item.getHvincuayn() );
        emgcRltmResponseDto.hvcrrtayn( item.getHvcrrtayn() );
        emgcRltmResponseDto.hvecmoayn( item.getHvecmoayn() );
        emgcRltmResponseDto.hvoxyayn( item.getHvoxyayn() );
        emgcRltmResponseDto.hvhypoayn( item.getHvhypoayn() );
        emgcRltmResponseDto.hvamyn( item.getHvamyn() );
        emgcRltmResponseDto.hv1( item.getHv1() );
        if ( item.getHv2() != null ) {
            emgcRltmResponseDto.hv2( item.getHv2() );
        }
        if ( item.getHv3() != null ) {
            emgcRltmResponseDto.hv3( item.getHv3() );
        }
        if ( item.getHv4() != null ) {
            emgcRltmResponseDto.hv4( item.getHv4() );
        }
        emgcRltmResponseDto.hv5( item.getHv5() );
        if ( item.getHv6() != null ) {
            emgcRltmResponseDto.hv6( item.getHv6() );
        }
        emgcRltmResponseDto.hv7( item.getHv7() );
        if ( item.getHv8() != null ) {
            emgcRltmResponseDto.hv8( item.getHv8() );
        }
        if ( item.getHv9() != null ) {
            emgcRltmResponseDto.hv9( item.getHv9() );
        }
        emgcRltmResponseDto.hv10( item.getHv10() );
        emgcRltmResponseDto.hv11( item.getHv11() );
        emgcRltmResponseDto.hv12( item.getHv12() );
        if ( item.getHv13() != null ) {
            emgcRltmResponseDto.hv13( item.getHv13() );
        }
        if ( item.getHv14() != null ) {
            emgcRltmResponseDto.hv14( item.getHv14() );
        }
        if ( item.getHv15() != null ) {
            emgcRltmResponseDto.hv15( item.getHv15() );
        }
        if ( item.getHv16() != null ) {
            emgcRltmResponseDto.hv16( item.getHv16() );
        }
        if ( item.getHv17() != null ) {
            emgcRltmResponseDto.hv17( item.getHv17() );
        }
        if ( item.getHv18() != null ) {
            emgcRltmResponseDto.hv18( item.getHv18() );
        }
        if ( item.getHv19() != null ) {
            emgcRltmResponseDto.hv19( item.getHv19() );
        }
        if ( item.getHv21() != null ) {
            emgcRltmResponseDto.hv21( item.getHv21() );
        }
        if ( item.getHv22() != null ) {
            emgcRltmResponseDto.hv22( item.getHv22() );
        }
        if ( item.getHv23() != null ) {
            emgcRltmResponseDto.hv23( item.getHv23() );
        }
        if ( item.getHv24() != null ) {
            emgcRltmResponseDto.hv24( item.getHv24() );
        }
        if ( item.getHv25() != null ) {
            emgcRltmResponseDto.hv25( item.getHv25() );
        }
        if ( item.getHv26() != null ) {
            emgcRltmResponseDto.hv26( item.getHv26() );
        }
        if ( item.getHv27() != null ) {
            emgcRltmResponseDto.hv27( item.getHv27() );
        }
        if ( item.getHv28() != null ) {
            emgcRltmResponseDto.hv28( item.getHv28() );
        }
        if ( item.getHv29() != null ) {
            emgcRltmResponseDto.hv29( item.getHv29() );
        }
        if ( item.getHv30() != null ) {
            emgcRltmResponseDto.hv30( item.getHv30() );
        }
        if ( item.getHv31() != null ) {
            emgcRltmResponseDto.hv31( item.getHv31() );
        }
        if ( item.getHv32() != null ) {
            emgcRltmResponseDto.hv32( item.getHv32() );
        }
        if ( item.getHv33() != null ) {
            emgcRltmResponseDto.hv33( item.getHv33() );
        }
        if ( item.getHv34() != null ) {
            emgcRltmResponseDto.hv34( item.getHv34() );
        }
        if ( item.getHv35() != null ) {
            emgcRltmResponseDto.hv35( item.getHv35() );
        }
        if ( item.getHv36() != null ) {
            emgcRltmResponseDto.hv36( item.getHv36() );
        }
        if ( item.getHv37() != null ) {
            emgcRltmResponseDto.hv37( item.getHv37() );
        }
        if ( item.getHv38() != null ) {
            emgcRltmResponseDto.hv38( item.getHv38() );
        }
        if ( item.getHv39() != null ) {
            emgcRltmResponseDto.hv39( item.getHv39() );
        }
        if ( item.getHv40() != null ) {
            emgcRltmResponseDto.hv40( item.getHv40() );
        }
        if ( item.getHv41() != null ) {
            emgcRltmResponseDto.hv41( item.getHv41() );
        }
        emgcRltmResponseDto.hv42( item.getHv42() );
        emgcRltmResponseDto.hv43( item.getHv43() );
        if ( item.getHv60() != null ) {
            emgcRltmResponseDto.hv60( item.getHv60() );
        }
        if ( item.getHv61() != null ) {
            emgcRltmResponseDto.hv61( item.getHv61() );
        }
        emgcRltmResponseDto.dutyName( item.getDutyName() );
        emgcRltmResponseDto.dutyTel3( item.getDutyTel3() );
        if ( item.getHvs01() != null ) {
            emgcRltmResponseDto.hvs01( item.getHvs01() );
        }
        if ( item.getHvs02() != null ) {
            emgcRltmResponseDto.hvs02( item.getHvs02() );
        }
        if ( item.getHvs03() != null ) {
            emgcRltmResponseDto.hvs03( item.getHvs03() );
        }
        if ( item.getHvs04() != null ) {
            emgcRltmResponseDto.hvs04( item.getHvs04() );
        }
        if ( item.getHvs05() != null ) {
            emgcRltmResponseDto.hvs05( item.getHvs05() );
        }
        if ( item.getHvs06() != null ) {
            emgcRltmResponseDto.hvs06( item.getHvs06() );
        }
        if ( item.getHvs07() != null ) {
            emgcRltmResponseDto.hvs07( item.getHvs07() );
        }
        if ( item.getHvs08() != null ) {
            emgcRltmResponseDto.hvs08( item.getHvs08() );
        }
        if ( item.getHvs09() != null ) {
            emgcRltmResponseDto.hvs09( item.getHvs09() );
        }
        if ( item.getHvs10() != null ) {
            emgcRltmResponseDto.hvs10( item.getHvs10() );
        }
        if ( item.getHvs11() != null ) {
            emgcRltmResponseDto.hvs11( item.getHvs11() );
        }
        if ( item.getHvs12() != null ) {
            emgcRltmResponseDto.hvs12( item.getHvs12() );
        }
        if ( item.getHvs13() != null ) {
            emgcRltmResponseDto.hvs13( item.getHvs13() );
        }
        if ( item.getHvs14() != null ) {
            emgcRltmResponseDto.hvs14( item.getHvs14() );
        }
        if ( item.getHvs15() != null ) {
            emgcRltmResponseDto.hvs15( item.getHvs15() );
        }
        if ( item.getHvs16() != null ) {
            emgcRltmResponseDto.hvs16( item.getHvs16() );
        }
        if ( item.getHvs17() != null ) {
            emgcRltmResponseDto.hvs17( item.getHvs17() );
        }
        if ( item.getHvs18() != null ) {
            emgcRltmResponseDto.hvs18( item.getHvs18() );
        }
        if ( item.getHvs19() != null ) {
            emgcRltmResponseDto.hvs19( item.getHvs19() );
        }
        if ( item.getHvs20() != null ) {
            emgcRltmResponseDto.hvs20( item.getHvs20() );
        }
        if ( item.getHvs21() != null ) {
            emgcRltmResponseDto.hvs21( item.getHvs21() );
        }
        if ( item.getHvs22() != null ) {
            emgcRltmResponseDto.hvs22( item.getHvs22() );
        }
        if ( item.getHvs23() != null ) {
            emgcRltmResponseDto.hvs23( item.getHvs23() );
        }
        if ( item.getHvs24() != null ) {
            emgcRltmResponseDto.hvs24( item.getHvs24() );
        }
        if ( item.getHvs25() != null ) {
            emgcRltmResponseDto.hvs25( item.getHvs25() );
        }
        if ( item.getHvs26() != null ) {
            emgcRltmResponseDto.hvs26( item.getHvs26() );
        }
        if ( item.getHvs27() != null ) {
            emgcRltmResponseDto.hvs27( item.getHvs27() );
        }
        if ( item.getHvs28() != null ) {
            emgcRltmResponseDto.hvs28( item.getHvs28() );
        }
        if ( item.getHvs29() != null ) {
            emgcRltmResponseDto.hvs29( item.getHvs29() );
        }
        if ( item.getHvs30() != null ) {
            emgcRltmResponseDto.hvs30( item.getHvs30() );
        }
        if ( item.getHvs31() != null ) {
            emgcRltmResponseDto.hvs31( item.getHvs31() );
        }
        if ( item.getHvs32() != null ) {
            emgcRltmResponseDto.hvs32( item.getHvs32() );
        }
        if ( item.getHvs33() != null ) {
            emgcRltmResponseDto.hvs33( item.getHvs33() );
        }
        if ( item.getHvs34() != null ) {
            emgcRltmResponseDto.hvs34( item.getHvs34() );
        }
        if ( item.getHvs35() != null ) {
            emgcRltmResponseDto.hvs35( item.getHvs35() );
        }
        if ( item.getHvs36() != null ) {
            emgcRltmResponseDto.hvs36( item.getHvs36() );
        }
        if ( item.getHvs37() != null ) {
            emgcRltmResponseDto.hvs37( item.getHvs37() );
        }
        if ( item.getHvs38() != null ) {
            emgcRltmResponseDto.hvs38( item.getHvs38() );
        }
        if ( item.getHvs46() != null ) {
            emgcRltmResponseDto.hvs46( item.getHvs46() );
        }
        if ( item.getHvs47() != null ) {
            emgcRltmResponseDto.hvs47( item.getHvs47() );
        }
        if ( item.getHvs48() != null ) {
            emgcRltmResponseDto.hvs48( item.getHvs48() );
        }
        if ( item.getHvs49() != null ) {
            emgcRltmResponseDto.hvs49( item.getHvs49() );
        }
        if ( item.getHvs50() != null ) {
            emgcRltmResponseDto.hvs50( item.getHvs50() );
        }
        if ( item.getHvs51() != null ) {
            emgcRltmResponseDto.hvs51( item.getHvs51() );
        }
        if ( item.getHvs52() != null ) {
            emgcRltmResponseDto.hvs52( item.getHvs52() );
        }
        if ( item.getHvs53() != null ) {
            emgcRltmResponseDto.hvs53( item.getHvs53() );
        }
        if ( item.getHvs54() != null ) {
            emgcRltmResponseDto.hvs54( item.getHvs54() );
        }
        if ( item.getHvs55() != null ) {
            emgcRltmResponseDto.hvs55( item.getHvs55() );
        }
        if ( item.getHvs56() != null ) {
            emgcRltmResponseDto.hvs56( item.getHvs56() );
        }
        if ( item.getHvs57() != null ) {
            emgcRltmResponseDto.hvs57( item.getHvs57() );
        }
        if ( item.getHvs58() != null ) {
            emgcRltmResponseDto.hvs58( item.getHvs58() );
        }
        if ( item.getHvs59() != null ) {
            emgcRltmResponseDto.hvs59( item.getHvs59() );
        }
        if ( item.getHvs60() != null ) {
            emgcRltmResponseDto.hvs60( item.getHvs60() );
        }
        if ( item.getHvs61() != null ) {
            emgcRltmResponseDto.hvs61( item.getHvs61() );
        }

        return emgcRltmResponseDto.build();
    }
}
